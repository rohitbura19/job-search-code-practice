# Q03. Card Discount Calculation

**Duration:** 25 minutes · **Language:** Java · **Source:** IDFC sample sheet

> The rules printed on the sample sheet don't produce its own expected output. Applied in order to 100 and 50, they give 150 → 135 → 115, not **130**. The reading below is the only one that matches the 130. In the real round, **state this ambiguity and ask before you code**. The panel grades that as a positive.

## Problem Statement

A customer pays with one or more cards, and each card has a value. Find the **minimum payable amount** after applying the discount rules.

Let `total` be the sum of all card values. Then:

1. Work out each discount the customer is eligible for:
   - **A. 10% off:** `total × 0.90`. Always eligible.
   - **B. Flat ₹20 off:** `total − 20`, never less than 0. Always eligible.
   - **C. 20% off:** `total × 0.80`. Eligible **only if** the minimum card value is ≥ 5 **and** there are **more than 2 cards**.
2. `payable` is the **lowest** of the eligible amounts. Only one of A, B or C is applied; they don't stack.
3. If `payable` is still **more than ₹200**, take off another **₹50**.
4. Return the amount rounded to 2 decimal places, rounding half up.

## Function

```java
BigDecimal minimumPayable(List<Integer> cardValues)
```

## Examples

| Input | total | A (10%) | B (−20) | C (20%) | best | > 200? | **Output** |
|---|---|---|---|---|---|---|---|
| `[100, 50]` | 150 | 135 | 130 | not eligible (2 cards) | 130 | no | **130.00** |
| `[100, 100, 100]` | 300 | 270 | 280 | 240 | 240 | yes, −50 | **190.00** |
| `[10, 20]` | 30 | 27 | 10 | not eligible | 10 | no | **10.00** |
| `[4, 100, 200]` | 304 | 273.6 | 284 | not eligible (min card 4) | 273.6 | yes, −50 | **223.60** |
| `[10]` | 10 | 9 | 0 (floor at 0) | not eligible | 0 | no | **0.00** |
| `[90, 90, 90]` | 270 | 243 | 250 | 216 | 216 | yes, −50 | **166.00** |

## Expected Design

- Use `BigDecimal` for money, not `double`, and explain why.
- `interface DiscountRule { boolean isEligible(Cart c); BigDecimal apply(BigDecimal total); }`, implemented by `PercentageDiscount`, `FlatDiscount` and `ConditionalPercentageDiscount`. This is the Strategy pattern.
- `DiscountEngine` holds a list of rules, chooses the minimum, then applies a `PostDiscountRule` for the extra ₹50.
- Adding a new rule shouldn't require changing the engine (open/closed principle).

## Constraints and validation

- A null or empty list, or any negative value, throws `IllegalArgumentException`.
- Card values are at most 10^6 and there are at most 10^4 cards. Can `int` overflow? Use `long` for the sum.

## Edge cases to discuss

Total of exactly 200 (no extra discount) · a total that is 200 or less before the extra rule but more than 200 after a discount, and in which order those rules apply · the flat discount taking the amount below 0 · exactly 2 cards against 3 cards · a card worth exactly 5.

## Follow-ups

1. Allow two discounts to stack (10% then ₹20) when a business flag is on.
2. Read the rules from configuration or a database instead of code (rules engine, chain of responsibility).
3. Return an explanation of which rule won (an audit trail), as banks require.
4. Make it concurrent-safe and cacheable for a checkout service.
