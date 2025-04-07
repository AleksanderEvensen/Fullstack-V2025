ALTER TABLE refresh_tokens
    DROP FOREIGN KEY FK_REFRESH_TOKENS_ON_USER;

ALTER TABLE listings
    DROP FOREIGN KEY FK_LISTINGS_ON_SELLER;

ALTER TABLE listing_bookmarks
    DROP FOREIGN KEY FK_LISTING_BOOKMARKS_ON_USER;

ALTER TABLE user_roles
    DROP FOREIGN KEY fk_userol_on_user;

ALTER TABLE users
    DROP COLUMN id;

ALTER TABLE users
    ADD id BINARY(16) NOT NULL PRIMARY KEY;

ALTER TABLE listings
    DROP COLUMN seller_id;

ALTER TABLE listings
    ADD seller_id BINARY(16) NOT NULL;

ALTER TABLE listing_bookmarks
    DROP COLUMN user_id;

ALTER TABLE listing_bookmarks
    ADD user_id BINARY(16) NOT NULL;

ALTER TABLE refresh_tokens
    DROP COLUMN user_id;

ALTER TABLE refresh_tokens
    ADD user_id BINARY(16) NOT NULL;

ALTER TABLE user_roles
    DROP COLUMN user_id;

ALTER TABLE user_roles
    ADD user_id BINARY(16) NOT NULL;

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE listings
    ADD CONSTRAINT FK_LISTINGS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES users (id);

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT FK_LISTING_BOOKMARKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE chat_messages
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    sender_id    BINARY(16)            NOT NULL,
    recipient_id BINARY(16)            NOT NULL,
    content      VARCHAR(2000)         NOT NULL,
    timestamp    datetime              NULL,
    `read`       BIT(1)                NOT NULL,
    CONSTRAINT pk_chat_messages PRIMARY KEY (id)
);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id);