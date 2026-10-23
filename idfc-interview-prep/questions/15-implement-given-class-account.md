# Q15. Implement the Methods of a Given Class: Bank Account

**Duration:** 30 minutes · **Language:** Java · **Source:** IDFC online assessment, per LeetCode ("implement methods in a Java class", easy to medium). Bank-themed, like IDFC's domain.

## Problem Statement

You are given the class skeleton below. **Don't change the method signatures.** Implement every method so that the rules hold.

```java
public enum TxnType { DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST }
public record Txn(TxnType type, BigDecimal amount, LocalDate date, BigDecimal balanceAfter) {}

public class Account {
    public static final BigDecimal MIN_BALANCE = new BigDecimal("1000");
    public static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("50000");
    public static final BigDecimal ANNUAL_RATE = new BigDecimal("0.04");

    public Account(String id, BigDecimal openingBalance, Clock clock) { }  // opening balance >= MIN_BALANCE
    public BigDecimal balance() { }
    public void deposit(BigDecimal amount) { }
    public void withdraw(BigDecimal amount) { }
    public void transfer(Account to, BigDecimal amount) { }
    public BigDecimal applyMonthlyInterest() { }        // returns the interest credited
    public List<Txn> statement(LocalDate from, LocalDate to) { }  // inclusive, oldest first, unmodifiable
}
```

## Rules

1. Every amount must be **> 0**. Otherwise throw `IllegalArgumentException`.
2. `withdraw` and `transfer` must never leave the balance **below `MIN_BALANCE`**. If they would, throw `InsufficientFundsException`, a custom checked or unchecked exception (your choice; justify it).
3. Withdrawals plus outgoing transfers on the **same calendar day** (from `clock`) can't exceed `DAILY_WITHDRAWAL_LIMIT`. If they would, throw `DailyLimitExceededException`.
4. `transfer` is **atomic**: either both accounts change, or neither does. Transferring to the same account is an `IllegalArgumentException`.
5. Monthly interest = `balance × ANNUAL_RATE / 12`, rounded to 2 decimals, half up.
6. A failed operation must leave **no** partial state: no transaction recorded and no change to the daily total.

## Example

```
day 1: A = new Account("A", 10000)
A.deposit(5000)            -> balance 15000
A.withdraw(14500)          -> InsufficientFundsException (would leave 500 < 1000)
A.withdraw(14000)          -> balance 1000

day 1: B = new Account("B", 200000)
B.withdraw(30000)          -> balance 170000
B.withdraw(25000)          -> DailyLimitExceededException (55000 > 50000 today)
day 2: B.withdraw(25000)   -> balance 145000

day 2: A.transfer(B, 500)  -> InsufficientFundsException; A stays 1000, B stays 145000
day 2: B.transfer(A, 10000)-> B 135000, A 11000   (B's day-2 total is 35000, within the limit)
A.applyMonthlyInterest()   -> 36.67  (11000 x 0.04 / 12 = 36.666...), A = 11036.67
A.statement(day1, day2)    -> DEPOSIT 5000, WITHDRAWAL 14000, TRANSFER_IN 10000, INTEREST 36.67
```

## What the panel checks

- `BigDecimal` used with `compareTo`, not `equals`. Know why `new BigDecimal("2.0").equals(new BigDecimal("2.00"))` is false.
- Validation happens **before** any change, which keeps atomicity simple.
- Encapsulation: `statement()` returns an unmodifiable copy, and there's no public setter for the balance.
- `Clock` is injected, so "the same day" can be tested.

## Follow-ups

1. **Concurrency:** two threads run `A.transfer(B)` and `B.transfer(A)` at the same time. Show the deadlock, then fix it by **always locking the account with the smaller id first**.
2. Savings vs current accounts: a different minimum balance and no interest. Would you use inheritance or a policy object?
3. Idempotency: a client retries a transfer after a timeout. Add a `requestId` and deduplicate.
4. Move this to Spring Boot with JPA: `@Transactional`, optimistic locking with `@Version`, and what happens on `OptimisticLockException`.
