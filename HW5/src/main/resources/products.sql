CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL ,
    quantity INT CHECK (quantity >= 0),
    price NUMERIC(12, 2) CHECK (price > 0),
    image_url TEXT
);