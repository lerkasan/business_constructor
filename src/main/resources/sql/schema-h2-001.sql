CREATE TABLE business_type (
                id IDENTITY NOT NULL,
                title VARCHAR(1000) NOT NULL,
                code_kved VARCHAR(5) NOT NULL,
                CONSTRAINT business_type_id PRIMARY KEY (id),
                UNIQUE (code_kved),
                UNIQUE (title)
);

CREATE TABLE questionnaire (
                id IDENTITY NOT NULL,
                title VARCHAR(1000) NOT NULL,
                business_type_id BIGINT NOT NULL,
                CONSTRAINT questionnaire_id PRIMARY KEY (id),
                FOREIGN KEY (business_type_id) REFERENCES business_type(id),
                UNIQUE (title)
);

ALTER TABLE question
ADD questionnaire_id BIGINT NOT NULL;

ALTER TABLE question
ADD CONSTRAINT question_questionnaire_fk
FOREIGN KEY (questionnaire_id)
REFERENCES questionnaire (id)
ON DELETE CASCADE
ON UPDATE NO ACTION;
