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

CREATE TABLE input_type (
                id IDENTITY NOT NULL,
                title VARCHAR(255) NOT NULL,
                CONSTRAINT input_type_id PRIMARY KEY (id)
);


CREATE TABLE question (
                id IDENTITY NOT NULL,
                text VARCHAR(3000) NOT NULL,
                parent_id BIGINT,
                next_id BIGINT,
                input_type_id BIGINT NOT NULL,
                CONSTRAINT question_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX question_text_idx
ON question (text);

CREATE UNIQUE INDEX input_type_title_idx
ON input_type (title);

/*
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

ALTER TABLE question ADD CONSTRAINT input_type_question_fk
FOREIGN KEY (input_type_id)
REFERENCES input_type (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
*/

