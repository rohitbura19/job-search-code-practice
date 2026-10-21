# Answers: A2-pascal-triangle

(Spoilers. Open only after your attempt.)

**Reference**
```java
static List<List<Integer>> pascal(int rows) {
    List<List<Integer>> out = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
        List<Integer> row = new ArrayList<>();
        for (int c = 0; c <= r; c++) row.add(c == 0 || c == r ? 1 : out.get(r - 1).get(c - 1) + out.get(r - 1).get(c));   // each cell = the two above it
        out.add(row);
    }
    return out;
}
static List<Integer> pascalRow(int k) {                    // O(k) space, O(k^2) time
    Integer[] row = new Integer[k + 1]; Arrays.fill(row, 1);
    for (int r = 2; r <= k; r++) for (int c = r - 1; c >= 1; c--) row[c] += row[c - 1];   // right to left: row[c-1] is still the OLD value
    return Arrays.asList(row);
}
static List<Long> pascalRowMul(int k) {                    // O(k) time
    List<Long> r = new ArrayList<>(); long v = 1;
    for (int c = 0; c <= k; c++) { r.add(v); v = v * (k - c) / (c + 1); }
    return r;
}
```
Verified: the 5-row output above, `pascalRow(4) = [1, 4, 6, 4, 1]`, `pascalRow(0) = [1]`, `pascalRow(1) = [1, 1]`, `pascalRow(3) = [1, 3, 3, 1]`; `pascalRow(k)` and `pascalRowMul(k)` agree for every k from 0 to 30. The middle of row 33 is 1,166,803,110 (fits `int`); the middle of row 34 is 2,333,606,220, which does NOT fit: the `int` version printed `-1961361076` (silent overflow; the usual limit "row index at most 33" exists for this reason). Updating left to right would overwrite `row[c-1]` before it is used for `row[c]`, so the loop goes right to left (the same reason as the 0/1 knapsack in 1D). The closed form is `C(k, c) = k! / (c! (k-c)!)`; the multiplicative step is exact because `C(k, c) * (k - c)` equals `C(k, c+1) * (c + 1)`, so the product is divisible by `c + 1`; always multiply BEFORE dividing, and note that `v * (k - c)` can overflow a `long` earlier than the result does. For `C(n, k) mod p` with a prime `p`: factorials and inverse factorials mod `p` (Fermat), or Lucas' theorem when `n >= p`. Product uses: number of monotone paths in a grid, probabilities of `k` successes in `n` trials, and the Fenwick-style "sums of binomials" in counting questions. Common mistakes: `row[c] += row[c-1]` left to right, an off-by-one in the row length, and `int` overflow beyond row 33.
