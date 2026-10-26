> Code: `src/main/java/practice/d20261026/c2/DecodeWays.java`. Test: `DecodeWaysTest`. **25 min.**

# C2. Decode ways (1-D DP) + list every decoding

Messages are encoded `A=1, B=2, ..., Z=26`.

**(a) `numDecodings(String digits)`**: the number of ways to decode the string. `"12"` -> 2 (`AB`, `L`), `"226"` -> 3 (`BBF`, `BZ`, `VF`), `"06"` -> 0, `"10"` -> 1. The empty string has exactly 1 decoding (the empty message). Leading zeros never decode and `"0"` alone is invalid.

**(b) `decodeAll(String digits)`**: all decodings as letter strings in lexicographic order (called only for length <= 14).

Constraints: (a) length up to `10^6`, but for length 90 the answer must still fit in a `long`. Return `long`.

Say out loud: the recurrence and the two ways a `0` can be fine/fatal (`10`, `20` vs `30`, `100`); why the table collapses to two variables; at what length the count overflows a `long` (it is Fibonacci for a string of ones) and what you would return in a real API; why (b)'s natural generation order is already lexicographic.
