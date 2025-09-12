CREATE TABLE order_item
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    qty INTEGER NOT NULL,
    unit_price NUMERIC(15, 2) NOT NULL CHECK (unit_price >= 0)
);