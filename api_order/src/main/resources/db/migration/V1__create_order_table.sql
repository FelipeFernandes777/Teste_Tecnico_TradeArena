CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE order_status AS ENUM ('CREATED', 'PAID', 'CANCELLED');

CREATE TABLE orders
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status     order_status   NOT NULL,
    total      NUMERIC(15, 2) NOT NULL CHECK (total >= 0),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);