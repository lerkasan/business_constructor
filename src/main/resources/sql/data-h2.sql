INSERT INTO role (title) VALUES ('ROLE_USER');
INSERT INTO role (title) VALUES ('ROLE_ADMIN');
INSERT INTO role (title) VALUES ('ROLE_EXPERT');

INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('user1', 'user1@mail.com', '$2a$10$cHVXytfvSSDn5TAm5T/SfedKlSbEzBvFt2Y.AOqgA5jfhIOAlgZ4m',
   now());
INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('user2', 'user2@mail.com', '$2a$10$cHVXytfvSSDn5TAm5T/SfedKlSbEzBvFt2Y.AOqgA5jfhIOAlgZ4m',
   now());
INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('admin1', 'admin1@mail.com', '$2a$10$cHVXytfvSSDn5TAm5T/SfedKlSbEzBvFt2Y.AOqgA5jfhIOAlgZ4m',
   now());
INSERT INTO user (username, email, password_hash, creation_date) VALUES
  ('admin2', 'admin2@mail.com', '$2a$10$cHVXytfvSSDn5TAm5T/SfedKlSbEzBvFt2Y.AOqgA5jfhIOAlgZ4m',
   now());
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
INSERT INTO user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO user_role (user_id, role_id) VALUES (3, 2);
INSERT INTO user_role (user_id, role_id) VALUES (4, 2);
INSERT INTO user_role (user_id, role_id) VALUES (5, 1);
INSERT INTO user_role (user_id, role_id) VALUES (6, 2);
INSERT INTO user_role (user_id, role_id) VALUES (7, 3);

INSERT INTO permit_type (id, name) VALUES
  (1, 'permitType1');
INSERT INTO permit_type (id, name) VALUES
  (2, 'permitType2');

INSERT INTO permit (id, name, permit_type_id, legal_document_id, form_id, number, file_example, term, propose, status) VALUES
  (1, 'permit1', 1, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
INSERT INTO permit (id, name, permit_type_id, legal_document_id, form_id, number, filee_example, term, propose, status) VALUES
  (2, 'permit2', 1, 1, 1, ' ', '453d7a34', ' ', ' ', 1);
INSERT INTO permit (id, name, permit_type_id, legal_document_id, form_id, number, filee_example, term, propose, status) VALUES
  (3, 'permit3', 2, 1, 1, ' ', '453d7a34', ' ', ' ', 1);

INSERT INTO legal_document (id, id_rada, id_liga, id_state, date_pub, date_add, number_pub, title, number_rada, number_mj, in_rada, in_liga, in_brdo, auto_liga, auto_brdo, regulation, manual_sector, tech_regulation) VALUES
  (1, 'idRada1', 'idLiga1', 1, 1, 1 ,'numberPub1', 'title1','numberRada1','numberMj1', 1, 1, 1, 1, 1, 1, 'manualSector1', 1);
INSERT INTO legal_document (id, id_rada, id_liga, id_state, date_pub, date_add, number_pub, title, number_rada, number_mj, in_rada, in_liga, in_brdo, auto_liga, auto_brdo, regulation, manual_sector, tech_regulation) VALUES
  (2, 'idRada2', 'idLiga2', 2, 2, 2 ,'numberPub2', 'title2','numberRada2','numberMj2', 2, 2, 2, 2, 2, 2, 'manualSector2', 2);

INSERT INTO input_type(title) VALUES('checkbox');
INSERT INTO input_type(title) VALUES('droplist');
INSERT INTO input_type(title) VALUES('radiobutton');
INSERT INTO input_type(title) VALUES('text');