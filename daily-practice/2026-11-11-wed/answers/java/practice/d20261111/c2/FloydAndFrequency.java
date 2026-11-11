package practice.d20261111.c2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** findDuplicate: treat i -> nums[i] as a linked list; a repeated value is two edges into one node, so the list has a cycle and its entrance is the answer.
 *  Floyd: meet inside the cycle, restart one pointer at the head, step both by one: they meet at the entrance (the distance head->entrance equals meeting point->entrance modulo the cycle length).
 *  cycleStart: the same two phases on ListNode. topKFrequent: count, then bucket[f] = values with frequency f, read buckets from high to low. O(n) / O(n). */
public final class FloydAndFrequency {
    private FloydAndFrequency() {}

    public static int findDuplicate(int[] nums) {
        int slow = nums[0], fast = nums[nums[0]];
        while (slow != fast) { slow = nums[slow]; fast = nums[nums[fast]]; }
        slow = 0;
        while (slow != fast) { slow = nums[slow]; fast = nums[fast]; }
        return slow;
    }

    public static ListNode cycleStart(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) { slow = slow.next; fast = fast.next; }
                return slow;
            }
        }
        return null;
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : nums) freq.merge(x, 1, Integer::sum);
        List<Integer>[] buckets = new List[nums.length + 1];
        freq.forEach((v, f) -> { if (buckets[f] == null) buckets[f] = new ArrayList<>(); buckets[f].add(v); });
        int[] out = new int[k];
        int n = 0;
        for (int f = nums.length; f >= 1 && n < k; f--)
            if (buckets[f] != null) for (int v : buckets[f]) { if (n == k) break; out[n++] = v; }
        return out;
    }
}
