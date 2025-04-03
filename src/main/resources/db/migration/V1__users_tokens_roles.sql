CREATE TABLE refresh_tokens
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    token      VARCHAR(255)          NOT NULL,
    expires_at datetime              NOT NULL,
    user_id    BIGINT                NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    first_name        VARCHAR(255)          NOT NULL,
    last_name         VARCHAR(255)          NOT NULL,
    email             VARCHAR(255)          NOT NULL,
    password          VARCHAR(255)          NOT NULL,
    phone_number      VARCHAR(255)          NOT NULL,
    profile_image_url VARCHAR(255)          NULL,
    created_at        datetime              NULL,
    street_name       VARCHAR(255)          NULL,
    street_number     VARCHAR(255)          NULL,
    postal_code       VARCHAR(255)          NULL,
    city              VARCHAR(255)          NULL,
    country           VARCHAR(255)          NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_token UNIQUE (token);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phonenumber UNIQUE (phone_number);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);