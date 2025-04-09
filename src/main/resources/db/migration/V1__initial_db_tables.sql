CREATE TABLE categories
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255)          NOT NULL,
    `description`      VARCHAR(255)          NOT NULL,
    translation_string VARCHAR(255)          NOT NULL,
    icon               VARCHAR(255)          NOT NULL,
    version            BIGINT                NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE chat_messages
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    sender_id    BIGINT                NULL,
    recipient_id BIGINT                NULL,
    listing_id   BIGINT                NOT NULL,
    content      VARCHAR(2000)         NOT NULL,
    timestamp    datetime              NULL,
    `read`       BIT(1)                NOT NULL,
    CONSTRAINT pk_chat_messages PRIMARY KEY (id)
);

CREATE TABLE image_metadata
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    file_name     VARCHAR(255)          NOT NULL,
    original_name VARCHAR(255)          NOT NULL,
    content_type  VARCHAR(255)          NOT NULL,
    size          BIGINT                NOT NULL,
    upload_date   datetime              NOT NULL,
    CONSTRAINT pk_image_metadata PRIMARY KEY (id)
);

CREATE TABLE listing_bookmarks
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    listing_id BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    CONSTRAINT pk_listing_bookmarks PRIMARY KEY (id)
);

CREATE TABLE listing_defects
(
    listing_id BIGINT       NOT NULL,
    defect     VARCHAR(255) NULL
);

CREATE TABLE listing_images
(
    listing_id BIGINT       NOT NULL,
    image_url  VARCHAR(255) NULL
);

CREATE TABLE listing_modifications
(
    listing_id   BIGINT       NOT NULL,
    modification VARCHAR(255) NULL
);

CREATE TABLE listings
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    title              VARCHAR(255)          NOT NULL,
    category_id        BIGINT                NOT NULL,
    listing_condition  VARCHAR(255)          NOT NULL,
    seller_id          BIGINT                NOT NULL,
    price              DOUBLE                NOT NULL,
    original_price     DOUBLE                NULL,
    `description`      TEXT                  NOT NULL,
    model_year         INT                   NULL,
    manufacturer       VARCHAR(255)          NULL,
    model              VARCHAR(255)          NULL,
    serial_number      VARCHAR(255)          NULL,
    purchase_date      VARCHAR(255)          NULL,
    usage_duration     VARCHAR(255)          NULL,
    reason_for_selling VARCHAR(255)          NULL,
    created_at         datetime              NULL,
    updated_at         datetime              NULL,
    CONSTRAINT pk_listings PRIMARY KEY (id)
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
    version           BIGINT                NOT NULL,
    street_name       VARCHAR(255)          NULL,
    street_number     VARCHAR(255)          NULL,
    postal_code       VARCHAR(255)          NULL,
    city              VARCHAR(255)          NULL,
    country           VARCHAR(255)          NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT uc_364245fd2412fe9021e88fff0 UNIQUE (listing_id, user_id);

ALTER TABLE categories
    ADD CONSTRAINT uc_categories_name UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phonenumber UNIQUE (phone_number);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_LISTING FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id);

ALTER TABLE chat_messages
    ADD CONSTRAINT FK_CHAT_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE listings
    ADD CONSTRAINT FK_LISTINGS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE listings
    ADD CONSTRAINT FK_LISTINGS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES users (id);

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT FK_LISTING_BOOKMARKS_ON_LISTING FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT FK_LISTING_BOOKMARKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE listing_defects
    ADD CONSTRAINT fk_listing_defects_on_listing FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE listing_images
    ADD CONSTRAINT fk_listing_images_on_listing FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE listing_modifications
    ADD CONSTRAINT fk_listing_modifications_on_listing FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);