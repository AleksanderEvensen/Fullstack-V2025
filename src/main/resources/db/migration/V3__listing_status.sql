ALTER TABLE listings
    ADD listing_status SMALLINT NULL;

ALTER TABLE listings
    MODIFY listing_status SMALLINT NOT NULL;