# Answers: A1-contains-duplicate-ii

(Spoilers. Open only after your attempt.)

**Reference**
- Idea: map value -> last index seen. If current index minus last index <= k, return true. Memory-bounded variant: keep a HashSet of the last k elements (sliding window), evict `nums[i-k]`.
- Time O(n), space O(min(n,k)).
```java
public boolean containsNearbyDuplicate(int[] nums, int k) {
    Set<Integer> window = new HashSet<>();
    for (int i = 0; i < nums.length; i++) {
        if (i > k) window.remove(nums[i - k - 1]);
        if (!window.add(nums[i])) return true;
    }
    return false;
}
```
