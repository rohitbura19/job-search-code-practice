> Code: `src/main/java/practice/d20261029/a1/Roman.java`. Test: `RomanTest`. **15 min.**

# A1. Roman numerals both ways

1. `toInt(String s)`: Roman numeral (1..3999) to int. Symbols: I=1 V=5 X=10 L=50 C=100 D=500 M=1000. A smaller symbol before a bigger one is subtracted (IV=4, IX=9, XL=40, XC=90, CD=400, CM=900).
2. `toRoman(int n)`: canonical numeral for 1..3999; outside the range throw `IllegalArgumentException`.

Examples: `"III"` = 3, `"LVIII"` = 58, `"MCMXCIV"` = 1994; `toRoman(49)` = `"XLIX"`.

Constraints: input of `toInt` is a valid numeral (an unknown symbol throws `IllegalArgumentException`); empty string = 0. Single pass, O(1) extra space.
Say out loud: why scanning right to left (or looking one ahead) works, and why the greedy table in `toRoman` must contain the six subtractive pairs.
