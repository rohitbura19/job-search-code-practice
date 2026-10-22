# Answers: C2-combination-sum

(Spoilers. Open only after your attempt.)

**Reference**
```java
public List<List<Integer>> combinationSum(int[] c, int target) {
    Arrays.sort(c);                                       // enables early break
    List<List<Integer>> out = new ArrayList<>();
    dfs(c, 0, target, new ArrayDeque<>(), out);
    return out;
}
private void dfs(int[] c, int start, int rem, Deque<Integer> path, List<List<Integer>> out) {
    if (rem == 0) { out.add(new ArrayList<>(path)); return; }   // copy the path!
    for (int i = start; i < c.length && c[i] <= rem; i++) {     // sorted -> prune
        path.addLast(c[i]);
        dfs(c, i, rem - c[i], path, out);                       // i, not i+1: reuse allowed
        path.removeLast();                                      // undo (the "backtrack")
    }
}
```
Passing `start = i` (never going back to earlier indices) is what prevents permutations of the same set. Combination Sum II: sort, recurse with `i + 1`, and `if (i > start && c[i] == c[i-1]) continue;` to skip duplicates at the same depth. Count only: unbounded-knapsack DP `dp[s] += dp[s - coin]` with coins in the outer loop, O(target × n). Worst case is exponential (about `n^(target/min)`), output-sensitive.
