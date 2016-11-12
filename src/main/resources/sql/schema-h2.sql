CREATE TABLE role (
  id    IDENTITY     NOT NULL,
  title VARCHAR(255) NOT NULL,
  CONSTRAINT role_id PRIMARY KEY (id)
);


CREATE UNIQUE INDEX user_role_title_idx
ON role (title);

CREATE TABLE user (
  id                      IDENTITY             NOT NULL,
  username                VARCHAR(255)         NOT NULL,
  first_name              VARCHAR(255),
  middle_name             VARCHAR(255),
  last_name               VARCHAR(255),
  email                   VARCHAR(255)         NOT NULL,
  password_hash           VARCHAR(100)         NOT NULL,
  creation_date           DATE                 NOT NULL,
  is_notification_enabled BOOLEAN DEFAULT TRUE NOT NULL,
  CONSTRAINT user_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX user_username_idx
ON user (username);

CREATE UNIQUE INDEX user_email_idx
ON user (email);

CREATE TABLE user_role (
  id      IDENTITY NOT NULL,
  user_id BIGINT   NOT NULL,
  role_id BIGINT   NOT NULL,
  CONSTRAINT user_role_id PRIMARY KEY (id)
);

CREATE TABLE UserConnection (
  userId         VARCHAR(255) NOT NULL,
  providerId     VARCHAR(255) NOT NULL,
  providerUserId VARCHAR(255),
  rank           INT          NOT NULL,
  displayName    VARCHAR(255),
  profileUrl     VARCHAR(512),
  imageUrl       VARCHAR(512),
  accessToken    VARCHAR(512) NOT NULL,
  secret         VARCHAR(512),
  refreshToken   VARCHAR(512),
  expireTime     BIGINT,
  CONSTRAINT user_connection_id PRIMARY KEY (userId, providerId, providerUserId)
);

CREATE UNIQUE INDEX UserConnectionRank
ON UserConnection (userId, providerId, rank);