CREATE TABLE carts
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_carts_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE cart_items
(
    id       BIGSERIAL PRIMARY KEY,
    cart_id  BIGINT  NOT NULL,
    book_id  BIGINT  NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),

    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id)
            REFERENCES carts (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_cart_items_book
        FOREIGN KEY (book_id)
            REFERENCES books (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_cart_items_cart_book UNIQUE (cart_id, book_id)
);

CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    order_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    user_id    BIGINT                      NOT NULL,

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE order_items
(
    id       BIGSERIAL PRIMARY KEY,
    order_id BIGINT  NOT NULL,
    book_id  BIGINT  NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_order_items_book
        FOREIGN KEY (book_id)
            REFERENCES books (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_carts_user_id ON carts (user_id);
CREATE INDEX idx_cart_items_cart_id ON cart_items (cart_id);
CREATE INDEX idx_cart_items_book_id ON cart_items (book_id);
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_book_id ON order_items (book_id);