ALTER TABLE listings
    ADD latitude DOUBLE NULL;

ALTER TABLE listings
    ADD longitude DOUBLE NULL;

ALTER TABLE listings
    MODIFY latitude DOUBLE NOT NULL;

ALTER TABLE listings
    MODIFY longitude DOUBLE NOT NULL;