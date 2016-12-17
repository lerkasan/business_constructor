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
                PRIMARY KEY (user_id, role_id),
                FOREIGN KEY (user_id) REFERENCES user(id),
                FOREIGN KEY (role_id) REFERENCES role(id)
);

DROP TABLE IF EXISTS permit;

DROP TABLE IF EXISTS permit_type;

CREATE TABLE permit_type (
  id   BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255)          NOT NULL,
  CONSTRAINT permit_type_id PRIMARY KEY (id),
  UNIQUE (name)
);

CREATE TABLE permit (
  id                 BIGINT AUTO_INCREMENT NOT NULL,
  name               VARCHAR(750)          NOT NULL,
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
  UNIQUE (name(255))
);

DROP TABLE IF EXISTS question;

CREATE TABLE question (
                id BIGINT AUTO_INCREMENT NOT NULL,
                text VARCHAR(3000) NOT NULL,
                input_type VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);

DROP TABLE IF EXISTS option_;

CREATE TABLE option_ (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(500) NOT NULL,
                question_id BIGINT NOT NULL,
                procedure_id BIGINT,
                next_question_id BIGINT,
                PRIMARY KEY (id),
                FOREIGN KEY (question_id) REFERENCES question(id)
);

CREATE TABLE procedure_type (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255)  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE procedure_ (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR (2048)  NOT NULL,
  reason     VARCHAR (2048)    NOT NULL,
  result  VARCHAR (2048)       NOT NULL,
  permit_id          BIGINT        NOT NULL,
  procedure_type_id  BIGINT    NOT NULL,
  cost      VARCHAR (2048)    NOT NULL,
  term      VARCHAR (2048)    NOT NULL,
  method VARCHAR(2048) NOT NULL ,
  decision VARCHAR (2048)    NOT NULL,
  deny VARCHAR (2048)    NOT NULL,
  abuse VARCHAR (2048)   NOT NULL,
  CONSTRAINT procedure_id PRIMARY KEY (id),
  FOREIGN KEY (permit_id) REFERENCES permit(id),
  FOREIGN KEY (procedure_type_id) REFERENCES procedure_type(id)
);

CREATE TABLE procedure_document (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR (255) NOT NULL ,
  procedure_id          BIGINT        NOT NULL,
  example_file BLOB,
  CONSTRAINT procedure_id PRIMARY KEY (id),
  FOREIGN KEY (procedure_id ) REFERENCES procedure_(id)
);

CREATE UNIQUE INDEX procedureNameIndx
ON procedure_document (name);


ALTER TABLE option_ ADD CONSTRAINT option_question_fk
FOREIGN KEY (question_id)
REFERENCES question (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE option_ ADD CONSTRAINT option_next_question_fk
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

INSERT INTO procedure_type (name) VALUES
  ('procedureType1');
INSERT INTO procedure_type (name) VALUES
  ('procedureType2');
INSERT INTO procedure_type (name) VALUES
  ('procedureType3');

INSERT INTO procedure_ (name, reason, result, permit_id, procedure_type_id, cost, term, method, decision, deny, abuse)
VALUES ('procedure2', 'reason2', 'result2', 1, 2, 'coast2', 'term2', 'method2', 'decision2', 'deny2', 'abuse2');

INSERT INTO procedure_ (name, reason, result, permit_id, procedure_type_id, cost, term, method, decision, deny, abuse)
VALUES ('procedure3', 'reason3', 'result3', 2, 3, 'coast3', 'term3', 'method3', 'decision3', 'deny3', 'abuse3');

INSERT INTO procedure_ (name, reason, result, permit_id, procedure_type_id, cost, term, method, decision, deny, abuse)
VALUES ('procedure1', 'reason1', 'result1', 3, 1, 'coast1', 'term1', 'method1', 'decision1', 'deny1', 'abuse1');

INSERT INTO procedure_ (name, reason, result, permit_id, procedure_type_id, cost, term, method, decision, deny, abuse)
VALUES ('procedure4', 'reason4', 'result4', 1, 1, 'coast4', 'term4', 'method4', 'decision4', 'deny4', 'abuse4');

INSERT INTO procedure_document (name, procedure_id, example_file) VALUES ('procedure_document1', 1, '453d7a34');
INSERT INTO procedure_document (name, procedure_id, example_file) VALUES ('procedure_document3', 2, '453d7a34');
INSERT INTO procedure_document (name, procedure_id, example_file) VALUES ('procedure_document2', 3, '453d7a34');