CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255)
);