CREATE TABLE books
(
    id     BIGINT PRIMARY KEY,
    title  VARCHAR(255)   NOT NULL,
    author VARCHAR(255)   NOT NULL,
    isbn   VARCHAR(13)    NOT NULL,
    type   VARCHAR(10)    NOT NULL,
    price  DECIMAL(10, 2) NOT NULL
);