> Code: `src/main/java/practice/d20261021/a1/AddBinary.java`, `ExcelColumn.java`. Tests: `AddBinaryTest`, `ExcelColumnTest`.

### A1. Add Binary, then Excel Sheet Column Title (carry arithmetic and bijective base-26)
(a) Add two binary strings and return the sum as a binary string, without converting to `int`/`long`. `"11" + "1" -> "100"`; `"1010" + "1011" -> "10101"`; `"0" + "0" -> "0"`.
(b) Excel column titles: `1 -> A`, `26 -> Z`, `27 -> AA`, `28 -> AB`, `52 -> AZ`, `701 -> ZY`, `702 -> ZZ`, `703 -> AAA`. Write `title(n)` and the inverse `number(s)`.

Interviewer follow-up: why does the loop condition include `carry > 0`? What changes for decimal strings of 10^5 digits (this is how `BigInteger` addition works, and why is it O(n))? Why does the "obvious" `n % 26` / `n / 26` conversion give the wrong answer for 26 (there is no zero digit, so the system is called bijective)? What is the title for `Integer.MAX_VALUE`? What does `number("")` return, and what happens for `"a"` (lower case)? Multiply two binary strings (grade-school, O(n·m)) and say what Karatsuba changes.
