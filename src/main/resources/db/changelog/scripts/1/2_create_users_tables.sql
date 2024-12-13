-- V1__Create_user_and_roles.sql

-- 1. Создание таблицы user_roles
CREATE TABLE IF NOT EXISTS user_roles
(
    id    BIGINT PRIMARY KEY,
    title VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email    VARCHAR(100),
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES user_roles (id) ON DELETE CASCADE
);

INSERT INTO user_roles (id, title)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_GUEST')
ON CONFLICT (id) DO NOTHING;