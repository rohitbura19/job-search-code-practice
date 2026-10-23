> Code: `src/main/java/practice/d20261023/a1/Majority.java`. Test: `MajorityTest`.

# A1. Majority Element (> n/2) and Majority Element II (> n/3) - Boyer-Moore voting (12 min)

(a) `majorityElement(int[] nums)`: the value that appears more than `n/2` times (guaranteed to exist).
`[3,2,3] -> 3`, `[2,2,1,1,1,2,2] -> 2`.

(b) `majorityThird(int[] nums)`: all values that appear more than `n/3` times, ascending.
`[3,2,3] -> [3]`, `[1,2] -> [1,2]`, `[1,2,3] -> []`.

Constraints: `0 <= n <= 5*10^6`, values fit in `int`, O(n) time, O(1) extra space (no HashMap).

Say out loud: why cancelling pairs never removes a true majority; why (b) needs a second verification pass and (a) does not; how many candidates for "more than n/k"; what happens with `Integer.MIN_VALUE` and with two initial candidates that are equal.
