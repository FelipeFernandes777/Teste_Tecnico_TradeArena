CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE product
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku        VARCHAR(254)   NOT NULL UNIQUE,
    name       VARCHAR(254)   NOT NULL,
    price      NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    stock      INTEGER        NOT NULL CHECK (stock >= 0),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_sku ON product (sku);
CREATE INDEX idx_product_name ON product (name);