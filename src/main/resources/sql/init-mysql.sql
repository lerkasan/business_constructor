DROP TABLE IF EXISTS role;

CREATE TABLE role (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);


CREATE UNIQUE INDEX role_title_idx
ON role ( title );

DROP TABLE IF EXISTS user;

CREATE TABLE user (
                id BIGINT AUTO_INCREMENT NOT NULL,
                username VARCHAR(255),
                first_name VARCHAR(255),
                middle_name VARCHAR(255),
                last_name VARCHAR(255),
                email VARCHAR(255) NOT NULL,
                password_hash VARCHAR(60) NOT NULL,
                creation_date DATE NOT NULL,
                PRIMARY KEY (id)
);


CREATE UNIQUE INDEX user_email_idx
ON user ( email );

CREATE UNIQUE INDEX user_username_idx
ON user ( email );

DROP TABLE IF EXISTS user_role;

CREATE TABLE user_role (
                user_id BIGINT NOT NULL,
                role_id BIGINT NOT NULL,
                PRIMARY KEY (user_id, role_id)
);

DROP TABLE IF EXISTS permit;

DROP TABLE IF EXISTS permit_type;

CREATE TABLE permit_type (
  id   BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR               NOT NULL,
  CONSTRAINT permit_type_id PRIMARY KEY (id),
  UNIQUE KEY (name(255))
);


CREATE TABLE permit (
  id                 BIGINT AUTO_INCREMENT NOT NULL,
  name               VARCHAR               NOT NULL,
  permit_type_id     BIGINT                NOT NULL,
  legal_document_id  BIGINT                NOT NULL,
  form_id            BIGINT                NOT NULL,
  number             VARCHAR(11)           NOT NULL,
  file_example       BLOB,
  term               varchar(3000)         NOT NULL,
  propose            varchar(3000)         NOT NULL,
  status             TINYINT               NOT NULL,
  CONSTRAINT permit_id PRIMARY KEY (id),
  FOREIGN KEY (permit_type_id) REFERENCES permit_type(id),
  UNIQUE KEY (name(750))
);




DROP TABLE IF EXISTS input_type;

DROP TABLE IF EXISTS business_type;

CREATE TABLE business_type (
                id INT AUTO_INCREMENT NOT NULL,
                title VARCHAR(500) NOT NULL,
                kved VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);

CREATE TABLE input_type (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);

DROP TABLE IF EXISTS question;

CREATE TABLE question (
                id BIGINT AUTO_INCREMENT NOT NULL,
                text VARCHAR(3000) NOT NULL,
                input_type_id BIGINT NOT NULL DEFAULT 1,
                PRIMARY KEY (id)
);

DROP TABLE IF EXISTS option_;

CREATE TABLE option_ (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(1000) NOT NULL,
                PRIMARY KEY (id)
);

DROP TABLE IF EXISTS questionnaire;

CREATE TABLE questionnaire (
                id INT AUTO_INCREMENT NOT NULL,
                business_type_id INT NOT NULL,
                title VARCHAR(1000) NOT NULL,
                PRIMARY KEY (id)
);

DROP TABLE IF EXISTS question_questionnaire;

CREATE TABLE question_questionnaire (
                question_id BIGINT NOT NULL,
                questionnaire_id INT NOT NULL,
                PRIMARY KEY (question_id, questionnaire_id)
);

DROP TABLE IF EXISTS question_option;

CREATE TABLE question_option (
                id BIGINT AUTO_INCREMENT NOT NULL,
                question_id BIGINT NOT NULL,
                option_id BIGINT NOT NULL,
                procedure_id INT,
                next_question_id BIGINT,
                PRIMARY KEY (id)
);


DROP TABLE IF EXISTS user_answer;

CREATE TABLE user_answer (
                id BIGINT AUTO_INCREMENT NOT NULL,
                user_id BIGINT NOT NULL,
                question_option_id BIGINT NOT NULL,
                PRIMARY KEY (id)
);

ALTER TABLE question ADD CONSTRAINT input_type_question_fk
FOREIGN KEY (input_type_id)
REFERENCES input_type (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE question_questionnaire ADD CONSTRAINT question_question_questionnaire_fk
FOREIGN KEY (question_id)
REFERENCES question (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE question_option ADD CONSTRAINT question_question_option_fk
FOREIGN KEY (question_id)
REFERENCES question (id)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE question_option ADD CONSTRAINT question_question_option_fk1
FOREIGN KEY (option_id)
REFERENCES option_ (id)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE questionnaire ADD CONSTRAINT business_type_questionnaire_fk
FOREIGN KEY (business_type_id)
REFERENCES business_type (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE question_questionnaire ADD CONSTRAINT questionnaire_question_questionnaire_fk
FOREIGN KEY (questionnaire_id)
REFERENCES questionnaire (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE user_answer ADD CONSTRAINT question_option_user_answer_fk
FOREIGN KEY (question_option_id)
REFERENCES question_option (id)
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

insert into input_type(title) values("checkbox");
insert into input_type(title) values("droplist");
insert into input_type(title) values("radiobutton");
insert into input_type(title) values("text");

INSERT INTO role (title) VALUES ('ROLE_USER');
INSERT INTO role (title) VALUES ('ROLE_ADMIN');
INSERT INTO role (title) VALUES ('ROLE_EXPERT');
INSERT INTO role (title) VALUES ('ROLE_MODERATOR');

INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('some_user1', 'some_user1@mail.com', '$2a$10$cHVXytfvSSDn5TAm5T/SfedKlSbEzBvFt2Y.AOqgA5jfhIOAlgZ4m',
   now());
INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('admin', 'some_admin1@mail.com', '$2a$10$V/MgrWzBqwypVcu/RQMPt.iBYQlpHVHycyaGXCM.UwyeOLoSg6VwO',
   now());
INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('expert', 'some_expert1@mail.com', '$2a$10$U2JdYU2ypsJVS3SFZD8mue68PZLWXSySjrYKtVL44.uZl3KflfXRa',
   now());

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (2, 3);
INSERT INTO user_role (user_id, role_id) VALUES (3, 3);

INSERT INTO permit_type (id, name) VALUES
  (1, 'permitType1');
INSERT INTO permit_type (id, name) VALUES
  (2, 'permitType2');

INSERT INTO permit (id, name, permit_type_id, legal_document_id, form_id, number, file_example, term, propose, status) VALUES
  (1, 'permit1', 1, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
INSERT INTO permit (id, name, permit_type_id, legal_document_id, form_id, number, file_example, term, propose, status) VALUES
  (2, 'permit2', 1, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
INSERT INTO permit (id, name, permit_type_id, legal_document_id, form_id, number, file_example, term, propose, status) VALUES
  (3, 'permit3', 2, 1, 1, ' ', '453d7a34', ' ', ' ', 1);

DROP TABLE IF EXISTS legal_document;
CREATE TABLE legal_document (
  id                      BIGINT              NOT NULL  AUTO_INCREMENT,
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
INSERT INTO legal_document (id, id_rada, id_liga, id_state, date_pub, date_add, number_pub, title, number_rada, number_mj, in_rada, in_liga, in_brdo, auto_liga, auto_brdo, regulation, manual_sector, tech_regulation) VALUES
  (1, 'idRada1', 'idLiga1', 1, 1, 1 ,'numberPub1', 'title1','numberRada1','numberMj1', 1, 1, 1, 1, 1, 1, 'manualSector1', 1);
INSERT INTO legal_document (id, id_rada, id_liga, id_state, date_pub, date_add, number_pub, title, number_rada, number_mj, in_rada, in_liga, in_brdo, auto_liga, auto_brdo, regulation, manual_sector, tech_regulation) VALUES
  (2, 'idRada2', 'idLiga2', 2, 2, 2 ,'numberPub2', 'title2','numberRada2','numberMj2', 2, 2, 2, 2, 2, 2, 'manualSector2', 2);
