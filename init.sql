-- Crear la base de datos
--CREATE DATABASE IF NOT EXISTS ordersdb;

-- Crear tabla de clientes
CREATE TABLE customers (
    customer_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    active BOOLEAN DEFAULT TRUE
);

-- Insertar registros de prueba
INSERT INTO customers (customer_id, name, email, phone, active) VALUES
('customer-001', 'Alice Smith', 'alice@example.com', '1111111111', true),
('customer-002', 'Bob Johnson', 'bob@example.com', '2222222222', true),
('customer-003', 'Charlie Lee', 'charlie@example.com', '3333333333', true),
('customer-004', 'Diana Prince', 'diana@example.com', '4444444444', true),
('customer-005', 'Ethan Hunt', 'ethan@example.com', '5555555555', false);

CREATE TABLE products (
    product_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

INSERT INTO products (product_id, name, description, price, active) VALUES
('product-001', 'Laptop', 'Laptop de alto rendimiento con 16GB RAM y SSD 512GB', 999.99, true),
('product-002', 'Mouse', 'Mouse inalámbrico ergonómico', 25.50, true),
('product-003', 'Teclado', 'Teclado mecánico retroiluminado', 79.00, true),
('product-004', 'Monitor', 'Monitor 24 pulgadas Full HD', 149.99, true),
('product-005', 'Auriculares', 'Auriculares con cancelación de ruido', 129.90, false);

