ALTER TABLE listings
    DROP COLUMN created_at;

ALTER TABLE listings
    DROP COLUMN updated_at;

ALTER TABLE listings
    ADD created_at datetime NULL;

ALTER TABLE listings
    ADD updated_at datetime NULL;