--liquibase formatted sql

--changeset selyunina:1
CREATE TABLE IF NOT EXISTS postgres.wallet
(
    id uuid PRIMARY KEY ,
    balance NUMERIC(19,2) NOT NULL ,
    created_at TIMESTAMP NOT NULL
);
