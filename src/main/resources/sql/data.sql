INSERT INTO USER (id, username, password)
VALUES
  (1, 'user', 'password'),
  (2, 'user2', 'password'),
  (3, 'user3', 'password');

INSERT INTO role (id, role)
VALUES
  (1, 'ADMIN'),
  (2, 'GUEST'),
  (3, 'USER');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES
  (1, 1),
  (2, 2),
  (3, 3);
