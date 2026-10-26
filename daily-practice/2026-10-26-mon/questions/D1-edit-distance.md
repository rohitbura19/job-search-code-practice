> Code: `src/main/java/practice/d20261026/d1/EditDistance.java`. Test: `EditDistanceTest` (brute-force comparison + a 200,000-char band test). **25 min.**

# D1. Edit distance, then a banded version for huge strings

**(a) `minDistance(a, b)`**: the minimum number of single-character insertions, deletions and substitutions that turn `a` into `b`. `"horse" -> "ros"` = 3, `"intention" -> "execution"` = 5. Strings up to 5,000 characters: use O(min(n, m)) memory.

**(b) `distanceAtMost(a, b, k)`**: return the distance if it is `<= k`, else `-1`. Strings up to 200,000 characters and `k <= 10` (think spell-check / diff of near-identical files): an `n x m` table is 40 billion cells.

Say out loud: the three transitions and what each means as an edit; how a rolling row works and which neighbour you lose; why a path of cost `<= k` never leaves the band `|i - j| <= k`; what value you store outside the band and why capping at `k + 1` is safe; how you would also return the edit script (what must you keep).
