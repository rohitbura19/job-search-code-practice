> Code: `src/main/java/practice/d20261014/d1/EditDistance.java`. Test: `EditDistanceTest`.

### D1. Edit Distance (2D DP)
Minimum number of single-character insert, delete or replace operations to turn `word1` into `word2`. `"horse" -> "ros" = 3`; `"intention" -> "execution" = 5`.

Interviewer follow-up: reduce the space to O(min(n, m)). Print the actual operations. Strings are 10^5 long but you only care whether the distance is at most k = 3 — can you beat O(n·m)? Where would you use this at product level (hint: the fuzzy fallback in a typeahead design)?
