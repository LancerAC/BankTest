CREATE TABLE bank_account
(
    id              SERIAL PRIMARY KEY,
    current_balance DOUBLE PRECISION NOT NULL,
    initial_balance DOUBLE PRECISION NOT NULL
);