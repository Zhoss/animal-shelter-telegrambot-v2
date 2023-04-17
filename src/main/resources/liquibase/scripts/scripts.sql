-- liquibase formatted sql

-- changeset dzhosan:1
CREATE TABLE dogs
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    breed        VARCHAR(50) NOT NULL,
    coat_color   VARCHAR(50) NOT NULL,
    age          INTEGER     NOT NULL CHECK (age > 0),
    features     TEXT,
    is_taken     BOOLEAN     NOT NULL DEFAULT FALSE,
    on_probation BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE carers
(
    id              SERIAL PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    phone_number    TEXT,
    birth_year      BIGINT       NOT NULL CHECK (birth_year > 1900),
    passport_number TEXT,
    chat_id         BIGINT       NOT NULL,
    dog_id          BIGINT CHECK (dog_id > 0) REFERENCES dogs (id)
);

CREATE TABLE agreements
(
    id              SERIAL PRIMARY KEY,
    number          TEXT NOT NULL,
    conclusion_date DATE NOT NULL,
    carer_id        BIGINT CHECK (carer_id > 0) REFERENCES carers (id)
);

CREATE TABLE daily_reports
(
    id           SERIAL PRIMARY KEY,
    file_path    VARCHAR(255) NOT NULL,
    file_size    BIGINT       NOT NULL CHECK (file_size > 0),
    media_type   VARCHAR(255) NOT NULL,
    report_date  DATE         NOT NULL,
    dog_diet     TEXT         NOT NULL,
    dog_health   TEXT         NOT NULL,
    dog_behavior TEXT         NOT NULL,
    carer_id     BIGINT CHECK (carer_id > 0) REFERENCES carers (id)
);

CREATE TABLE volunteer_chats
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    telegram_chat_id BIGINT      NOT NULL
);