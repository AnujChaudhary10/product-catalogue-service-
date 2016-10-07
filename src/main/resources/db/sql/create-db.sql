DROP TABLE products IF EXISTS;

CREATE TABLE products (
  product_id   INTEGER PRIMARY KEY,
  product_name VARCHAR(50),
  product_type  VARCHAR(30)
);
