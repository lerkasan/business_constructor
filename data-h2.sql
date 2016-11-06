DROP TABLE IF EXISTS USER;

CREATE TABLE USER
(
  id       INT          NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO USER (id, username, password)
VALUES
  (1, 'user', 'password'),
  (2, 'user2', 'password'),
  (3, 'user3', 'password');

DROP TABLE IF EXISTS role;

CREATE TABLE ROLE
(
  id   INT          NOT NULL AUTO_INCREMENT,
  role VARCHAR(250) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO role (id, role)
VALUES
  (1, 'ADMIN'),
  (2, 'GUEST'),
  (3, 'USER');

DROP TABLE IF EXISTS user_roles;

CREATE TABLE USER_ROLES (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES ROLE (id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (user_id) REFERENCES USER (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO USER_ROLES (user_id, role_id)
VALUES
  (1, 1),
  (2, 2),
  (3, 3);
