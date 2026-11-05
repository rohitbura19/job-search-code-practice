# A2. String basics (10 min)

File `a2/StringBasics.java`. Test: `StringBasicsTest`.

1. `strStr(hay, needle)` - first index of `needle`, -1 if absent, 0 for an empty needle. No `indexOf`/`contains`. `("hello","ll") -> 2`.
2. `reverseWords(s)` - reverse word order, trim, collapse repeated spaces. `"  hello   world  " -> "world hello"`. Only `' '` separates words.
3. `zigzag(s, rows)` - write `s` in a zigzag over `rows` rows, read row by row. `("PAYPALISHIRING", 3) -> "PAHNAPLSIIGYIR"`; `rows = 1` returns `s`.

Constraints: `0 <= |s| <= 10^5`, `1 <= rows`. Aim: O(n) for 2 and 3; say what `strStr` costs in the worst case and how KMP fixes it.
