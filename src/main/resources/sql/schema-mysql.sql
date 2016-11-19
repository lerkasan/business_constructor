CREATE TABLE role (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);


CREATE UNIQUE INDEX role_title_idx
 ON role
 ( title );

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

CREATE TABLE user_role (
                user_id BIGINT NOT NULL,
                role_id BIGINT NOT NULL,
                PRIMARY KEY (user_id, role_id)
);

CREATE TABLE input_type (
                id BIGINT AUTO_INCREMENT NOT NULL,
                title VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
);

CREATE UNIQUE INDEX input_type_title_idx
 ON input_type
 ( title );

CREATE TABLE question (
                id BIGINT AUTO_INCREMENT NOT NULL,
                text VARCHAR(3000) NOT NULL,
                parent_id BIGINT,
                next_id BIGINT,
                input_type_id BIGINT NOT NULL,
                PRIMARY KEY (id)
);

/*

!!!!

ALTER TABLE user ADD CONSTRAINT user_role_id_fk
FOREIGN KEY (role_id)
REFERENCES role (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE question ADD CONSTRAINT input_type_question_fk
FOREIGN KEY (input_type_id)
REFERENCES input_type (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE user_answer ADD CONSTRAINT question_option_user_answer_fk
FOREIGN KEY (question_option_id)
REFERENCES question_option (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
*/