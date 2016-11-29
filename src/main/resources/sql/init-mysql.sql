DROP TABLE IF EXISTS role;

CREATE TABLE role (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);


CREATE UNIQUE INDEX role_title_idx
 ON role
 ( title );

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
 ON user
 ( email );

 CREATE UNIQUE INDEX user_username_idx
 ON user
 ( email );

DROP TABLE IF EXISTS user_role;

CREATE TABLE user_role (
                user_id BIGINT NOT NULL,
                role_id BIGINT NOT NULL,
                PRIMARY KEY (user_id, role_id)
);

DROP TABLE IF EXISTS permitType;

CREATE TABLE permitType (
  id   BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255)          NOT NULL,
  CONSTRAINT permitTypeId PRIMARY KEY (id)
);

CREATE UNIQUE INDEX permitTypeNameIndx
ON permitType (name);


DROP TABLE IF EXISTS permit;

CREATE TABLE permit (
  id               BIGINT AUTO_INCREMENT NOT NULL,
  name             VARCHAR(1023)         NOT NULL,
  permitTypeId     BIGINT                NOT NULL,
  legalDocumentId  BIGINT                NOT NULL,
  formId           BIGINT                NOT NULL,
  number           VARCHAR(11)           NOT NULL,
  fileExample      BLOB,
  term             varchar(3000)         NOT NULL,
  propose          varchar(3000)         NOT NULL,
  status           TINYINT               NOT NULL,
  CONSTRAINT permitId PRIMARY KEY (id)
);

CREATE UNIQUE INDEX permitNameIndx
ON permit (name);

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

INSERT INTO permitType (id, name) VALUES
  (1, 'permitType1');
INSERT INTO permitType (id, name) VALUES
  (2, 'permitType2');

INSERT INTO permit (id, name, permitTypeId, legaldocumentid, formid, number, fileExample, term, propose, status) VALUES
  (1, 'permit1', 1, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
INSERT INTO permit (id, name, permitTypeId, legaldocumentid, formid, number, fileExample, term, propose, status) VALUES
  (2, 'permit2', 1, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
INSERT INTO permit (id, name, permitTypeId, legaldocumentid, formid, number, fileExample, term, propose, status) VALUES
  (3, 'permit3', 2, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
