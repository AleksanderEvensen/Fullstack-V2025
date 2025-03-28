
CREATE TABLE users(
    id         BIGINT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100)      NOT NULL,
    password   VARCHAR(100)      NOT NULL,
    enabled    TINYINT DEFAULT 0 NOT NULL,
    created_at datetime          NULL,
    updated_at datetime          NULL
);