# Answers: A1-add-binary-excel-column

(Spoilers. Open only after your attempt.)

**Reference**
```java
static String addBinary(String a, String b) {
    StringBuilder sb = new StringBuilder(); int i = a.length() - 1, j = b.length() - 1, carry = 0;
    while (i >= 0 || j >= 0 || carry > 0) {                         // carry > 0: "1" + "1" needs a new leading digit
        int s = carry; if (i >= 0) s += a.charAt(i--) - '0'; if (j >= 0) s += b.charAt(j--) - '0';
        sb.append((char) ('0' + (s & 1))); carry = s >> 1;           // digit = s mod 2, carry = s / 2
    }
    return sb.length() == 0 ? "0" : sb.reverse().toString();         // both inputs empty -> "0"
}
static String title(int n) {
    StringBuilder sb = new StringBuilder();
    while (n > 0) { n--; sb.append((char) ('A' + n % 26)); n /= 26; }   // n-- shifts 1..26 to 0..25: that IS the bijective trick
    return sb.reverse().toString();
}
static int number(String s) { int n = 0; for (char c : s.toCharArray()) n = n * 26 + (c - 'A' + 1); return n; }
```
Verified: `100`, `10101`, `0`; `title` gives `A Z AA AB AZ ZY ZZ AAA` for `1 26 27 28 52 701 702 703` and `FXSHRXW` for `Integer.MAX_VALUE`; `number(title(n)) == n` for every n from 1 to 200,000; `addBinary` matched `BigInteger.add` on 5,000 random pairs of up to 80 bits. O(max(n, m)) time, O(max(n, m)) space for the result. Idea: add column by column from the right, keep the carry, and reverse at the end. In (b) there is no digit for zero (`A` is 1, not 0), so a plain base-26 conversion would turn 26 into "10" (`BA`); subtracting one BEFORE taking the remainder maps `1..26` to `0..25` at every position. `number` is the ordinary positional sum (Horner), valid because each letter is `1..26`. Decimal strings of 10^5 digits: the same loop with base 10 (use `s / 10` and `s % 10`), O(n); `BigInteger` does it limb by limb in `int` words. `number("")` is 0 and a lower-case letter gives a wrong number silently, so validate or upper-case the input first. Multiplication: for every digit pair add into position `i + j` and propagate carries once at the end, O(n·m); Karatsuba splits each number in halves and uses three recursive products instead of four, about O(n^1.585). Common mistakes: forgetting the final carry, appending digits in the wrong order, and using `int` for `n` in `title` when the input is a `long`.
