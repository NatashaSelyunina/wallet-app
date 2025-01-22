--liquibase formatted sql

--changeset selyunina:1
CREATE TABLE IF NOT EXISTS wallet
(
    id uuid PRIMARY KEY ,
    balance DECIMAL(19,2) NOT NULL ,
    created_at TIMESTAMP NOT NULL
);
