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
    model_year         VARCHAR(255)          NULL,
    manufacturer       VARCHAR(255)          NULL,
    model              VARCHAR(255)          NULL,
    serial_number      VARCHAR(255)          NULL,
    purchase_date      VARCHAR(255)          NULL,
    usage_duration     VARCHAR(255)          NULL,
    reason_for_selling VARCHAR(255)          NULL,
    CONSTRAINT pk_listings PRIMARY KEY (id)
);

ALTER TABLE categories
    ADD CONSTRAINT uc_categories_name UNIQUE (name);

ALTER TABLE listings
    ADD CONSTRAINT FK_LISTINGS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE listings
    ADD CONSTRAINT FK_LISTINGS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES users (id);

ALTER TABLE listing_defects
    ADD CONSTRAINT fk_listing_defects_on_listing FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE listing_images
    ADD CONSTRAINT fk_listing_images_on_listing FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE listing_modifications
    ADD CONSTRAINT fk_listing_modifications_on_listing FOREIGN KEY (listing_id) REFERENCES listings (id);