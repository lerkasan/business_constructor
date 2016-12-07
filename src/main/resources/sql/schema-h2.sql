CREATE TABLE role (
  id    IDENTITY     NOT NULL,
  title VARCHAR(255) NOT NULL,
  CONSTRAINT role_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX user_role_title_idx
ON role (title);

CREATE TABLE user (
  id                      IDENTITY             NOT NULL,
  username                VARCHAR(255)         NOT NULL,
  first_name              VARCHAR(255),
  middle_name             VARCHAR(255),
  last_name               VARCHAR(255),
  email                   VARCHAR(255)         NOT NULL,
  password_hash           VARCHAR(60)          NOT NULL,
  creation_date           DATE                 NOT NULL,
  CONSTRAINT user_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX user_username_idx
ON user (username);

CREATE UNIQUE INDEX user_email_idx
ON user (email);

CREATE TABLE user_role (
  id      IDENTITY NOT NULL,
  user_id BIGINT   NOT NULL,
  role_id BIGINT   NOT NULL,
  CONSTRAINT user_role_id PRIMARY KEY (id)
);

CREATE TABLE permit_type (
  id   IDENTITY      NOT NULL,
  name VARCHAR(255)  NOT NULL,
  CONSTRAINT permit_type_id PRIMARY KEY (id),
  UNIQUE KEY (name)
);


CREATE TABLE permit (
  id               IDENTITY      NOT NULL,
  name             VARCHAR(1023) NOT NULL,
  permit_type_id     BIGINT        NOT NULL,
  legal_document_id  BIGINT        NOT NULL,
  form_id           BIGINT        NOT NULL,
  number           VARCHAR(11)   NOT NULL,
  file_example      BLOB,
  term             LONGVARCHAR   NOT NULL,
  propose          LONGVARCHAR   NOT NULL,
  status           TINYINT       NOT NULL,
  CONSTRAINT permit_id PRIMARY KEY (id),
  FOREIGN KEY (permit_type_id) REFERENCES permit_type(id),
  UNIQUE KEY (name)
);

CREATE TABLE option_ (
                id IDENTITY NOT NULL,
                title VARCHAR(1000) NOT NULL,
                CONSTRAINT option_id PRIMARY KEY (id)
);


CREATE TABLE input_type (
                id IDENTITY NOT NULL,
                title VARCHAR(255) NOT NULL,
                CONSTRAINT input_type_id PRIMARY KEY (id)
);


CREATE TABLE question (
                id IDENTITY NOT NULL,
                text VARCHAR(3000) NOT NULL,
                input_type_id BIGINT NOT NULL DEFAULT 1,
                CONSTRAINT question_id PRIMARY KEY (id)
);


CREATE TABLE question_option (
                id IDENTITY NOT NULL,
                question_id BIGINT NOT NULL,
                option_id BIGINT NOT NULL,
                procedure_id INTEGER,
                next_question_id BIGINT,
                CONSTRAINT question_option_id PRIMARY KEY (id)
);

ALTER TABLE question_option ADD CONSTRAINT option__question_option_fk
FOREIGN KEY (option_id)
REFERENCES option_ (id)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE question ADD CONSTRAINT input_type_question_fk
FOREIGN KEY (input_type_id)
REFERENCES input_type (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE question_option ADD CONSTRAINT question_question_option_fk
FOREIGN KEY (question_id)
REFERENCES question (id)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE question_option ADD CONSTRAINT question_question_option_fk1
FOREIGN KEY (next_question_id)
REFERENCES question (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE user_role ADD CONSTRAINT user_role_user_id_fk
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE user_role ADD CONSTRAINT user_role_role_id_fk
FOREIGN KEY (role_id)
REFERENCES role (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

CREATE TABLE legal_document (
  id                      IDENTITY            NOT NULL,
  id_rada                 VARCHAR(50)         NOT NULL,
  id_liga                 VARCHAR(24)         NOT NULL,
  id_state                INTEGER             NOT NULL,
  date_pub                INTEGER             NOT NULL,
  date_add                INTEGER             NOT NULL,
  number_pub              VARCHAR(255)        NOT NULL,
  title                   VARCHAR(1025)       NOT NULL,
  number_rada             VARCHAR(255)        NOT NULL,
  number_mj               VARCHAR(65)         NOT NULL,
  in_rada                 TINYINT             NOT NULL,
  in_liga                 TINYINT             NOT NULL,
  in_brdo                 TINYINT             NOT NULL,
  auto_liga               TINYINT             NOT NULL,
  auto_brdo               TINYINT             NOT NULL,
  regulation              INTEGER             NOT NULL,
  manual_sector           VARCHAR(96)         NOT NULL,
  tech_regulation         INTEGER             NOT NULL,
  CONSTRAINT legal_document_id PRIMARY KEY (id)
);
