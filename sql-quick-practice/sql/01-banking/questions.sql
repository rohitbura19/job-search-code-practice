-- Banking SQL: JOIN, GROUP BY, HAVING, window functions  (IDFC Q13, 25 min)
-- Run schema.sql first. Write each query under its question, then compare with "Expected".
-- Tables: customers(id, name, city), accounts(id, customer_id, type, opened_on),
--         transactions(id, account_id, txn_type 'CREDIT'|'DEBIT', amount, txn_date)


-- Q1. Per account: customer name, account id, net balance (credits - debits). No transactions -> 0.
-- Expected: Asha 101 35000 | Asha 102 50000 | Bala 103 -5000 | Chen 104 15000 | Dev 105 0



-- Q2. Total DEBIT amount per city in October 2026, only cities with total > 20000, highest first.
-- Expected: Bangalore 50000 | Mumbai 45000



-- Q3. Customers who hold more than one account (name, count).
-- Expected: Asha 2



-- Q4. Customers whose accounts have no transactions at all.
-- Expected: Dev



-- Q5. Per city, the customer with the highest total credits (include ties).
-- Expected: Bangalore Asha 130000 | Mumbai Bala 40000



-- Q6. Accounts with a negative net balance.
-- Expected: 103
