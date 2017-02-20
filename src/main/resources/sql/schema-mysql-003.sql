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

CREATE TABLE answer (
                id                  BIGINT          NOT NULL  AUTO_INCREMENT,
                business_id         BIGINT          NOT NULL,
                option_id           BIGINT          NOT NULL,
                PRIMARY KEY (id),
                UNIQUE (business_id, option_id),
                FOREIGN KEY (business_id) REFERENCES business(id)
                  ON DELETE CASCADE,
                FOREIGN KEY (option_id) REFERENCES option_(id)
                  ON DELETE CASCADE
);

CREATE TABLE flow (
                id                  BIGINT          NOT NULL  AUTO_INCREMENT,
                business_id         BIGINT          NOT NULL,
                procedure_id        BIGINT          NOT NULL,
                priority            INT             NOT NULL,
                finished            BOOLEAN         NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (business_id) REFERENCES business(id)
                  ON DELETE CASCADE,
                FOREIGN KEY (procedure_id) REFERENCES procedure_(id)
                  ON DELETE CASCADE
);

ALTER TABLE questionnaire
  ADD draft  BOOLEAN  NOT NULL;