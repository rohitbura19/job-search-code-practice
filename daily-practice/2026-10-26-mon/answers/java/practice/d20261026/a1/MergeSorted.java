package practice.d20261026.a1;

/**
 * merge: fill a from the BACK so no unread element of a is overwritten: take the larger of a[i] and b[j]. Once b is used up,
 * the rest of a is already in place. O(m + n) time, O(1) space.
 * threeWayPartition (Dutch national flag): invariant a[0..lt) < pivot, a[lt..i) == pivot, a[gt+1..n) > pivot, a[i..gt] unknown.
 * A smaller element swaps with a[lt] (the swapped-in value is known to be == pivot, so i advances); a larger one swaps with
 * a[gt] (the swapped-in value is unknown, so i stays). Every step shrinks the unknown zone: O(n) time, O(1) space.
 * sortColors is the same partition with pivot 1.
 */
public final class MergeSorted {
    private MergeSorted() {}

    public static void merge(int[] a, int m, int[] b, int n) {
        int i = m - 1, j = n - 1, k = m + n - 1;
        while (j >= 0) a[k--] = (i >= 0 && a[i] > b[j]) ? a[i--] : b[j--];
    }

    public static void sortColors(int[] nums) { threeWayPartition(nums, 1); }

    public static int[] threeWayPartition(int[] a, int pivot) {
        int lt = 0, i = 0, gt = a.length - 1;
        while (i <= gt) {
            if (a[i] < pivot) swap(a, lt++, i++);
            else if (a[i] > pivot) swap(a, i, gt--);
            else i++;
        }
        return new int[]{lt, gt + 1};
    }

    private static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
}
