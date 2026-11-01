# D1. Shortest Palindrome (15 min)

`d1/ShortestPalindrome.java`

You may only **add** characters in front of `s`. Return the shortest palindrome you can obtain. `"aacecaaa" -> "aaacecaaa"`, `"abcd" -> "dcbabcd"`, `"" -> ""`.
Length up to 5 * 10^5: an O(n^2) palindrome scan fails the timeout. Target O(n) (KMP prefix function, or Z-function / rolling hash with a collision argument).
