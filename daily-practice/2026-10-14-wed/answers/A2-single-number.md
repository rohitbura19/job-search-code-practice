# Answers: A2-single-number

(Spoilers. Open only after your attempt.)

**Reference**
```java
static int singleNumber(int[] nums) { int x = 0; for (int n : nums) x ^= n; return x; }   // a^a = 0, a^0 = a, commutative

static int[] twoSingles(int[] nums) {
    int xor = 0; for (int n : nums) xor ^= n;          // = a ^ b, and a != b so xor != 0
    int low = xor & -xor;                              // lowest set bit: a and b differ here
    int a = 0, b = 0;
    for (int n : nums) if ((n & low) == 0) a ^= n; else b ^= n;   // split into two groups, pairs stay together
    return new int[]{a, b};
}
```
Verified: `4` and `[5, 3]`. Three times: count each bit position modulo 3 (`ones/twos` state machine, or loop over 32 bits summing and taking `% 3`). Power of two: `n > 0 && (n & (n - 1)) == 0`.
