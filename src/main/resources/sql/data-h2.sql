INSERT INTO role(title) VALUES ("user");
INSERT INTO role(title) VALUES ("admin");

INSERT INTO user(username,email,password_hash) VALUES ("user1", "user1@mail.com", "12345678");
INSERT INTO user(username,email,password_hash) VALUES ("admin2", "admin2@mail.com", "23456789");

INSERT INTO user_role(user_id,role_id) VALUES (0, 0);
INSERT INTO user_role(user_id,role_id) VALUES (1, 1);
