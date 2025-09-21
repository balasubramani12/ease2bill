-- =============================================
-- DATABASE: ease2bill
-- AUTHOR: You (Resume Project)
-- PURPOSE: Billing System for Spring Boot App
-- ENTITIES: User, Customer, Product, Invoice, Invoice_Item
-- RELATIONSHIPS:
--   Customer (1) → Invoice (M)
--   User (1) → Invoice (M)
--   Invoice (1) → Invoice_Item (M)
--   Product (1) → Invoice_Item (M)
-- =============================================

-- Drop DB if exists (optional — use only in dev)
-- DROP DATABASE IF EXISTS ease2bill;
CREATE DATABASE ease2bill;
USE ease2bill;

-- Disable FK checks for clean drop
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist
DROP TABLE IF EXISTS invoice_item;
DROP TABLE IF EXISTS invoice;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS user;

-- Re-enable FK checks
SET FOREIGN_KEY_CHECKS = 1;

-- ======================
-- 1. USER TABLE
-- ======================
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL, -- Store BCrypt encoded
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ======================
-- 2. CUSTOMER TABLE
-- ======================
CREATE TABLE customer (
    cust_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ======================
-- 3. PRODUCT TABLE
-- ======================
CREATE TABLE product (
    p_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100),
    mrp DECIMAL(10,2) NOT NULL,        -- Maximum Retail Price
    selling_price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,               -- Available quantity
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (selling_price <= mrp)       -- Business rule: SP should not exceed MRP
);

-- ======================
-- 4. INVOICE TABLE
-- ======================
CREATE TABLE invoice (
    invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cust_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    invoice_date DATE NOT NULL,
    invoice_time TIME NOT NULL,
    total_amt DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total_save DECIMAL(12,2) NOT NULL DEFAULT 0.00, -- Total Savings = Σ(MRP - SP)*Qty
    payment_status ENUM('PAID', 'UNPAID', 'PARTIAL') DEFAULT 'UNPAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cust_id) REFERENCES customer(cust_id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE RESTRICT
);

-- ======================
-- 5. INVOICE_ITEM TABLE
-- ======================
CREATE TABLE invoice_item (
    invoice_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    prod_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL,      -- Selling price at time of sale
    subtotal DECIMAL(12,2) NOT NULL,   -- quantity * price
    subsave DECIMAL(12,2) NOT NULL,    -- quantity * (MRP - selling_price)
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON DELETE CASCADE,
    FOREIGN KEY (prod_id) REFERENCES product(p_id) ON DELETE RESTRICT
);

-- ======================
-- SAMPLE DATA
-- ======================

-- Insert Users
INSERT INTO user (name, username, password, role) VALUES
('Alice Admin', 'alice', '$2a$10$slxVu3YdRjVpmXrR6kE48eXfT8eX1Wb6Q9VJZ6X8Y1Z2a3b4c5d6e', 'ADMIN'),
('Bob Cashier', 'bob', '$2a$10$slxVu3YdRjVpmXrR6kE48eXfT8eX1Wb6Q9VJZ6X8Y1Z2a3b4c5d6e', 'USER');

-- Insert Customers
INSERT INTO customer (name, phone, email, address) VALUES
('Rahul Sharma', '9876543210', 'rahul@email.com', '123 MG Road, Bangalore'),
('Priya Patel', '9123456789', 'priya@email.com', '456 Park Street, Mumbai');

-- Insert Products
INSERT INTO product (name, category, mrp, selling_price, stock) VALUES
('Dell Wireless Mouse', 'Electronics', 899.00, 699.00, 50),
('HP Laptop Sleeve', 'Accessories', 1299.00, 999.00, 30),
('Logitech Keyboard', 'Electronics', 1599.00, 1299.00, 25),
('Sony Headphones', 'Electronics', 2999.00, 2499.00, 20);

-- Insert Invoice (Bill #1)
INSERT INTO invoice (cust_id, user_id, invoice_date, invoice_time, total_amt, total_save, payment_status)
VALUES (1, 1, '2025-04-05', '10:30:00', 1998.00, 600.00, 'PAID');

-- Insert Invoice Items for Bill #1
INSERT INTO invoice_item (invoice_id, prod_id, quantity, price, subtotal, subsave) VALUES
(1, 1, 2, 699.00, 1398.00, 400.00),  -- 2 Mice: (899-699)*2 = 400
(1, 2, 1, 999.00, 999.00, 300.00);   -- 1 Sleeve: (1299-999)*1 = 300 → Total Save = 700? Wait...

-- ⚠️ Correction: Above total_save should be 700, not 600. Let’s fix invoice:
UPDATE invoice SET total_save = 700.00 WHERE invoice_id = 1;

-- Insert Invoice (Bill #2)
INSERT INTO invoice (cust_id, user_id, invoice_date, invoice_time, total_amt, total_save, payment_status)
VALUES (2, 2, '2025-04-05', '14:15:00', 3798.00, 900.00, 'PAID');

-- Insert Invoice Items for Bill #2
INSERT INTO invoice_item (invoice_id, prod_id, quantity, price, subtotal, subsave) VALUES
(2, 3, 2, 1299.00, 2598.00, 600.00), -- 2 Keyboards: (1599-1299)*2 = 600
(2, 4, 1, 2499.00, 2499.00, 500.00); -- 1 Headphone: (2999-2499) = 500 → Total Save = 1100? Fix:

UPDATE invoice SET total_save = 1100.00 WHERE invoice_id = 2;

-- ======================
-- ✅ DATABASE READY!
-- ======================

-- Optional: View sample data
SELECT * FROM user;
SELECT * FROM customer;
SELECT * FROM product;
SELECT * FROM invoice;
SELECT * FROM invoice_item;