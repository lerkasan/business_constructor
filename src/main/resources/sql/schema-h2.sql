CREATE TABLE role (
  id IDENTITY NOT NULL,
  title VARCHAR(255) NOT NULL,
  CONSTRAINT role_id PRIMARY KEY (id)
);


CREATE UNIQUE INDEX role_title_idx
ON role
( title );

CREATE TABLE user (
  id IDENTITY NOT NULL,
  username VARCHAR(255),
  first_name VARCHAR(255),
  middle_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  creation_timestamp DATE NOT NULL,
  is_notification_enabled BOOLEAN DEFAULT true NOT NULL,
  CONSTRAINT user_id PRIMARY KEY (id)
);


CREATE UNIQUE INDEX user_email_idx
ON user
( email );

CREATE TABLE user_role (
  user_id BIGINT NOT NULL,
  role_id INTEGER NOT NULL,
  CONSTRAINT user_role_id PRIMARY KEY (user_id, role_id)
);