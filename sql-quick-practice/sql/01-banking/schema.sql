-- Banking schema + sample data (IDFC Q13). Works in PostgreSQL, MySQL 8, H2 and SQLite.

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id   INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL
);

CREATE TABLE accounts (
    id          INT PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id),
    type        VARCHAR(10) NOT NULL,
    opened_on   DATE NOT NULL
);

CREATE TABLE transactions (
    id         INT PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts(id),
    txn_type   VARCHAR(10) NOT NULL,   -- 'CREDIT' or 'DEBIT'
    amount     INT NOT NULL,
    txn_date   DATE NOT NULL
);

INSERT INTO customers VALUES
    (1, 'Asha', 'Bangalore'), (2, 'Bala', 'Mumbai'), (3, 'Chen', 'Bangalore'), (4, 'Dev', 'Pune');

INSERT INTO accounts VALUES
    (101, 1, 'SAVINGS', '2024-01-10'), (102, 1, 'CURRENT', '2025-03-05'), (103, 2, 'SAVINGS', '2023-07-21'),
    (104, 3, 'SAVINGS', '2026-02-14'), (105, 4, 'SAVINGS', '2026-09-30');

INSERT INTO transactions VALUES
    (1, 101, 'CREDIT', 50000, '2026-10-01'), (2, 101, 'DEBIT', 12000, '2026-10-03'),
    (3, 102, 'CREDIT', 80000, '2026-10-05'), (4, 102, 'DEBIT', 30000, '2026-10-12'),
    (5, 103, 'CREDIT', 40000, '2026-09-28'), (6, 103, 'DEBIT', 45000, '2026-10-15'),
    (7, 104, 'CREDIT', 20000, '2026-10-02'), (8, 104, 'DEBIT', 5000, '2026-10-20'),
    (9, 101, 'DEBIT', 3000, '2026-10-21');
