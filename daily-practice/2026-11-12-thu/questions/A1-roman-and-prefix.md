# A1 - Roman numerals and common prefix (10 min) - `a1/RomanAndPrefix.java`

1. `romanToInt(String s)`: valid numeral, 1..3999. `"III"` -> 3, `"LVIII"` -> 58, `"MCMXCIV"` -> 1994.
2. `intToRoman(int n)`, 1..3999. `1994` -> `"MCMXCIV"`. `romanToInt(intToRoman(n)) == n` for all n.
3. `longestCommonPrefix(String[] strs)`: `{"flower","flow","flight"}` -> `"fl"`; `{"dog","racecar","car"}` -> `""`; empty array -> `""`.

Constraints: O(length) time, no regex, no sorting required.
