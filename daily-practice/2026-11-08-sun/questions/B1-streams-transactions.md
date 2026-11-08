# B1 - Streams on bank transactions (15 min) - `b1/TxnStreams.java`

`record Txn(String account, String category, long cents, LocalDate date)`; cents is signed (negative = spending). One stream pipeline per method.

1. `totalByAccount` - sum per account, keys sorted (TreeMap).
2. `topSpendCategories(txns, n)` - per account the n categories with the lowest total (biggest spend first), ties by name.
3. `latestPerAccountAndCategory` - one txn per (account, category): latest date, ties larger cents; sorted by account then category.
4. `countLargeAndSmall(txns, threshold)` - `abs(cents) >= threshold` counts under `true`, the rest under `false`; both keys always present.
5. `summary` - `"[A=45500, B=-3500]"` with `Collectors.joining`; `"[]"` for empty input.
