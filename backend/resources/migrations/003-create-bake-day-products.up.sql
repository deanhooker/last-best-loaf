CREATE TABLE bake_day_products (
  id SERIAL PRIMARY KEY,
  bake_day_id INTEGER NOT NULL REFERENCES bake_days(id) ON DELETE CASCADE,
  product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,

  inventory INTEGER NOT NULL CHECK (inventory >= 0),
  sold_count INTEGER NOT NULL DEFAULT 0 CHECK (sold_count >= 0),
  available BOOLEAN NOT NULL DEFAULT TRUE,

  price_override BIGINT,

  UNIQUE (bake_day_id, product_id)
);
