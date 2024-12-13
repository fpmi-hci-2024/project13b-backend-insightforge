CREATE TABLE IF NOT EXISTS authors
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS genres
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS books
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    poster      VARCHAR(500) NOT NULL,
    author_id   BIGINT,
    stock BIGINT,
    CONSTRAINT fk_author
        FOREIGN KEY (author_id)
            REFERENCES authors (id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS book_genres
(
    book_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, genre_id),
    CONSTRAINT fk_book
        FOREIGN KEY (book_id)
            REFERENCES books (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_genre
        FOREIGN KEY (genre_id)
            REFERENCES genres (id)
            ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_authors_name ON authors (name);
CREATE INDEX IF NOT EXISTS idx_genres_name ON genres (name);
CREATE INDEX IF NOT EXISTS idx_books_title ON books (title);

CREATE INDEX IF NOT EXISTS idx_books_author_id ON books (author_id);
CREATE INDEX IF NOT EXISTS idx_book_genres_genre_id ON book_genres (genre_id);