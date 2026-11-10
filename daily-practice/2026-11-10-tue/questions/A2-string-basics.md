# A2 - String basics (10 min) - `a2/StringBasics.java`

1. `compress(char[] chars)`: run-length encode in place. A run of length 1 stays a single char; longer runs become char + decimal length (`12` -> `'1','2'`). Return the new length. `aabbccc` -> `a2b2c3` (6). O(1) extra space.
2. `isValidIPv4(String s)`: four dot-separated decimal parts, each 0..255, no leading zeros except "0", digits only, no empty part. `"192.168.1.1"` valid; `"01.1.1.1"`, `"1.2.3"`, `"256.1.1.1"`, `"1.1.1."` invalid.
3. `addStrings(String a, String b)`: sum of two non-negative integers given as decimal strings (up to 10^6 digits). No BigInteger, no parsing the whole string. `"456" + "77"` -> `"533"`.
