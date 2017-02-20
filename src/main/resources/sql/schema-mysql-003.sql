CREATE TABLE business (
                id                  BIGINT          NOT NULL   AUTO_INCREMENT,
                title               VARCHAR(1000)   NOT NULL,
                business_type_id    BIGINT          NOT NULL,
                user_id             BIGINT          NOT NULL,
                creation_date       DATE            NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (business_type_id) REFERENCES business_type(id)
                  ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES user(id)
                  ON DELETE CASCADE
);

CREATE TABLE stage (
                id                  BIGINT          NOT NULL  AUTO_INCREMENT,
                priority            INT             NOT NULL,
                finished            BOOLEAN         NOT NULL,
                PRIMARY KEY (id)
);

CREATE TABLE answer (
                id                  BIGINT          NOT NULL  AUTO_INCREMENT,
                business_id         BIGINT          NOT NULL,
                option_id           BIGINT          NOT NULL,
                stage_id            BIGINT          NOT NULL,
                PRIMARY KEY (id),
                UNIQUE (business_id, option_id),
                FOREIGN KEY (business_id) REFERENCES business(id)
                  ON DELETE CASCADE,
                FOREIGN KEY (option_id) REFERENCES option_(id)
                  ON DELETE CASCADE,
                FOREIGN KEY (stage_id) REFERENCES stage(id)
                  ON DELETE CASCADE
);

ALTER TABLE questionnaire
  ADD draft  BOOLEAN  NOT NULL;