# Q13. Banking SQL: JOIN, GROUP BY, HAVING

**Duration:** 25 minutes on paper · **Source:** IDFC Technical Round 1, per LeetCode (a basic JOIN across several tables, then a GROUP BY). The candidate lost marks for using `WHERE` where `HAVING` was needed.

## Schema and sample data

```sql
customers(id, name, city)
  (1,'Asha','Bangalore'), (2,'Bala','Mumbai'), (3,'Chen','Bangalore'), (4,'Dev','Pune')

accounts(id, customer_id, type, opened_on)
  (101,1,'SAVINGS','2024-01-10'), (102,1,'CURRENT','2025-03-05'), (103,2,'SAVINGS','2023-07-21'),
  (104,3,'SAVINGS','2026-02-14'), (105,4,'SAVINGS','2026-09-30')

transactions(id, account_id, txn_type, amount, txn_date)
  (1,101,'CREDIT',50000,'2026-10-01'), (2,101,'DEBIT',12000,'2026-10-03'),
  (3,102,'CREDIT',80000,'2026-10-05'), (4,102,'DEBIT',30000,'2026-10-12'),
  (5,103,'CREDIT',40000,'2026-09-28'), (6,103,'DEBIT',45000,'2026-10-15'),
  (7,104,'CREDIT',20000,'2026-10-02'), (8,104,'DEBIT',5000,'2026-10-20'),
  (9,101,'DEBIT',3000,'2026-10-21')
```

## Questions (write the query, then check it against the expected result)

**1.** For each account, show the customer name, account id and net balance (credits − debits). Accounts with no transactions show 0.
```
Asha 101 35000 | Asha 102 50000 | Bala 103 -5000 | Chen 104 15000 | Dev 105 0
```
*Hint: `LEFT JOIN`, `COALESCE`, `SUM(CASE …)`.*

**2.** Total **debit** amount per city in **October 2026**. Show only cities with total debits **> 20000**, highest first.
```
Bangalore 50000 | Mumbai 45000
```
*The `txn_type` and date filters go in `WHERE`. The `> 20000` condition goes in `HAVING`.*

**3.** Customers who hold more than one account.
```
Asha 2
```

**4.** Customers whose accounts have **no transactions at all**.
```
Dev
```
*Hint: `LEFT JOIN … WHERE t.id IS NULL`, or `NOT EXISTS`. Be ready to explain the difference from `NOT IN` when there are NULLs.*

**5.** For each city, the customer with the highest total credits. Include ties.
```
Bangalore Asha 130000 | Mumbai Bala 40000
```
*Hint: `RANK() OVER (PARTITION BY city ORDER BY SUM(amount) DESC)`.*

**6.** Accounts with a negative net balance (overdrawn).
```
103
```

## Follow-up talk

- The logical order a query runs in: `FROM/JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY`. This explains why `WHERE` can't use `SUM(...)`.
- Which index helps Q2 (for example `transactions(account_id, txn_date)`), and what `EXPLAIN` should show.
- `INNER` vs `LEFT` join, and what happens to Q1 if you write `WHERE t.txn_type = 'CREDIT'` after a LEFT JOIN.
- Keeping balances consistent: a stored `balance` column updated in the same transaction vs computed on the fly. Isolation levels and `SELECT … FOR UPDATE`.
