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

CREATE TABLE dog_carers
(
    id              SERIAL PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    phone_number    TEXT,
    birth_year      BIGINT       NOT NULL CHECK (birth_year > 1900),
    passport_number TEXT,
    chat_id         BIGINT       NOT NULL,
    dog_id          BIGINT CHECK (dog_id > 0) REFERENCES dogs (id)
);

CREATE TABLE dog_agreements
(
    id                 SERIAL PRIMARY KEY,
    number             TEXT NOT NULL,
    conclusion_date    DATE NOT NULL,
    probation_end_data DATE NOT NULL,
    dog_carer_id           BIGINT CHECK (dog_carer_id > 0) REFERENCES dog_carers (id)
);

CREATE TABLE dog_daily_reports
(
    id           SERIAL PRIMARY KEY,
    file_path    VARCHAR(255) NOT NULL,
    file_size    BIGINT       NOT NULL CHECK (file_size > 0),
    media_type   VARCHAR(255) NOT NULL,
    report_date  DATE         NOT NULL,
    dog_diet     TEXT,
    dog_health   TEXT,
    dog_behavior TEXT,
    dog_carer_id     BIGINT CHECK (dog_carer_id > 0) REFERENCES dog_carers (id)
);

CREATE TABLE cats
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

CREATE TABLE cat_carers
(
    id              SERIAL PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    phone_number    TEXT,
    birth_year      BIGINT       NOT NULL CHECK (birth_year > 1900),
    passport_number TEXT,
    chat_id         BIGINT       NOT NULL,
    cat_id          BIGINT CHECK (cat_id > 0) REFERENCES cats (id)
);

CREATE TABLE cat_agreements
(
    id                 SERIAL PRIMARY KEY,
    number             TEXT NOT NULL,
    conclusion_date    DATE NOT NULL,
    probation_end_data DATE NOT NULL,
    cat_carer_id           BIGINT CHECK (cat_carer_id > 0) REFERENCES cat_carers (id)
);

CREATE TABLE cat_daily_reports
(
    id           SERIAL PRIMARY KEY,
    file_path    VARCHAR(255) NOT NULL,
    file_size    BIGINT       NOT NULL CHECK (file_size > 0),
    media_type   VARCHAR(255) NOT NULL,
    report_date  DATE         NOT NULL,
    cat_diet     TEXT,
    cat_health   TEXT,
    cat_behavior TEXT,
    cat_carer_id     BIGINT CHECK (cat_carer_id > 0) REFERENCES cat_carers (id)
);

CREATE TABLE volunteer_chats
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    pet_type         TEXT,
    telegram_chat_id BIGINT      NOT NULL
);

CREATE TABLE clients
(
    id               SERIAL PRIMARY KEY,
    telegram_chat_id BIGINT NOT NULL,
    pet_type         TEXT
)