-- liquibase formatted sql

-- changeset dzhosan:1
CREATE TABLE dogs
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    breed      VARCHAR(50) NOT NULL,
    coat_color VARCHAR(50) NOT NULL,
    age        INTEGER     NOT NULL CHECK (age > 0),
    features   TEXT,
    is_taken   BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE agreements
(
    id              SERIAL PRIMARY KEY,
    number          BIGINT NOT NULL CHECK (number > 0),
    conclusion_date DATE   NOT NULL
);

CREATE TABLE carers
(
    id              SERIAL PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    age             INTEGER CHECK (age > 18),
    passport_number TEXT,
    phone_number    TEXT,
    dog_id          BIGINT CHECK (dog_id > 0) REFERENCES dogs (id),
    agreement_id    BIGINT CHECK (agreement_id > 0) REFERENCES agreements (id)
);

ALTER TABLE dogs
    ADD carer_id BIGINT CHECK (carer_id > 0) REFERENCES carers (id);
ALTER TABLE agreements
    ADD carer_id BIGINT CHECK (carer_id > 0) REFERENCES carers (id);

CREATE TABLE daily_reports
(
    id          SERIAL PRIMARY KEY,
    carer_id    BIGINT CHECK (carer_id > 0) REFERENCES carers (id),
    dog_id      BIGINT CHECK (dog_id > 0) REFERENCES dogs (id),
    description TEXT         NOT NULL,
    photo       OID          NOT NULL,
    file_path   VARCHAR(255) NOT NULL,
    file_size   BIGINT       NOT NULL CHECK (file_size > 0),
    media_type  VARCHAR(255) NOT NULL
);

-- changeset dzhosan:2
DROP TABLE dogs CASCADE;

DROP TABLE agreements CASCADE;

ALTER TABLE carers
    ALTER COLUMN phone_number SET DATA TYPE char(16);
ALTER TABLE carers DROP COLUMN passport_number;
ALTER TABLE carers DROP COLUMN dog_id;
ALTER TABLE carers DROP COLUMN agreement_id;

ALTER TABLE daily_reports
    ADD report_date DATE NOT NULL;
ALTER TABLE daily_reports
    ADD dog_diet TEXT NOT NULL;
ALTER TABLE daily_reports
    ADD dog_health TEXT NOT NULL;
ALTER TABLE daily_reports
    ADD dog_behavior TEXT NOT NULL;


