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

-- changeset dzhosan:3
CREATE TABLE dogs
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    breed           VARCHAR(50) NOT NULL,
    coat_color      VARCHAR(50) NOT NULL,
    age             INTEGER     NOT NULL CHECK (age > 0),
    features        TEXT,
    is_taken        BOOLEAN     NOT NULL DEFAULT FALSE,
    on_probation    BOOLEAN     NOT NULL DEFAULT FALSE,
    carer_id        BIGINT CHECK (carer_id > 0) REFERENCES carers (id),
    daily_report_id BIGINT CHECK (daily_report_id > 0) REFERENCES daily_reports (id)
);

CREATE TABLE agreements
(
    id              SERIAL PRIMARY KEY,
    number          TEXT NOT NULL,
    conclusion_date DATE NOT NULL,
    carer_id        BIGINT CHECK (carer_id > 0) REFERENCES carers (id)
);

ALTER TABLE carers
    ADD COLUMN dog_id BIGINT CHECK (dog_id > 0) REFERENCES dogs (id);
ALTER TABLE carers
    ADD COLUMN agreement_id BIGINT CHECK (agreement_id > 0) REFERENCES agreements (id);
ALTER TABLE carers DROP COLUMN age;
ALTER TABLE carers
    ADD COLUMN birth_year BIGINT NOT NULL CHECK (birth_year > 1900);

CREATE TABLE volunteer_chats
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    telegram_chat_id BIGINT      NOT NULL
)
-- changeset sherbakov:1
ALTER TABLE carers
    ADD COLUMN chat_id BIGINT;

--changeset dzhosan:4
ALTER TABLE daily_reports
    ALTER COLUMN report_date DROP NOT NULL;
ALTER TABLE daily_reports
    ALTER COLUMN dog_diet DROP NOT NULL;
ALTER TABLE daily_reports
    ALTER COLUMN dog_health DROP NOT NULL;
ALTER TABLE daily_reports
    ALTER COLUMN dog_behavior DROP NOT NULL;

--changeset dzhosan:5
ALTER TABLE daily_reports
    DROP COLUMN description CASCADE;
ALTER TABLE daily_reports
    DROP COLUMN photo CASCADE;

--changeset dzhosan:6
ALTER TABLE dogs
    DROP COLUMN carer_id CASCADE;
ALTER TABLE dogs
    DROP COLUMN daily_report_id CASCADE;
ALTER TABLE carers
    ALTER COLUMN chat_id SET NOT NULL;
ALTER TABLE carers
    ALTER COLUMN phone_number SET NOT NULL;
ALTER TABLE carers
    ADD COLUMN passport_number TEXT;

--changeset dzhosan:7
ALTER TABLE carers
    DROP COLUMN agreement_id CASCADE;
ALTER TABLE daily_reports
    DROP COLUMN dog_id CASCADE;