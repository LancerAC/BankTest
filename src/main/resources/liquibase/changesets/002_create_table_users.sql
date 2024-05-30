SET search_path TO bank;


CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    user_name    VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    middle_name  VARCHAR(255),
    last_name    VARCHAR(255) NOT NULL,
    birth_date   DATE,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    account_id   BIGINT,
    FOREIGN KEY (account_id) REFERENCES bank_account (id)
);