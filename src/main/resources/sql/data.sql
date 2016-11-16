INSERT INTO USER (id, username, password)
VALUES
  (1, 'admin', '$2a$10$V/MgrWzBqwypVcu/RQMPt.iBYQlpHVHycyaGXCM.UwyeOLoSg6VwO'),
  (2, 'expert', '$2a$10$U2JdYU2ypsJVS3SFZD8mue68PZLWXSySjrYKtVL44.uZl3KflfXRa'),
  (3, 'user', '$2a$10$D6f/BQEOXiHfSmCbrvkvO.qg8/cE.dtM015D.ECpFgDEcQQRfE2Fm');

INSERT INTO role (id, role)
VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_EXPERT'),
  (3, 'ROLE_USER');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES
  (1, 1),
  (2, 2),
  (1, 2),
  (3, 3);

