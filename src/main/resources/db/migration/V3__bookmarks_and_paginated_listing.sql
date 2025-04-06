CREATE TABLE listing_bookmarks
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    listing_id BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    CONSTRAINT pk_listing_bookmarks PRIMARY KEY (id)
);

ALTER TABLE listings
    ADD created_at BIGINT NULL;

ALTER TABLE listings
    ADD updated_at BIGINT NULL;

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT uc_364245fd2412fe9021e88fff0 UNIQUE (listing_id, user_id);

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT FK_LISTING_BOOKMARKS_ON_LISTING FOREIGN KEY (listing_id) REFERENCES listings (id);

ALTER TABLE listing_bookmarks
    ADD CONSTRAINT FK_LISTING_BOOKMARKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);