ALTER TABLE users
    ADD name VARCHAR(255) NULL;

ALTER TABLE users
    MODIFY name VARCHAR(255) NOT NULL;

ALTER TABLE users
    DROP COLUMN first_name;

ALTER TABLE users
    DROP COLUMN last_name;