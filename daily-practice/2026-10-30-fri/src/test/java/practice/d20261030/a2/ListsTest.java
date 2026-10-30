package practice.d20261030.a2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ListsTest {
    @Test void reverseExamples() {
        assertArrayEquals(new int[] {3, 2, 1}, ListNode.toArray(Lists.reverse(ListNode.of(1, 2, 3))));
        assertArrayEquals(new int[] {1}, ListNode.toArray(Lists.reverse(ListNode.of(1))));
        assertNull(Lists.reverse(null));
        assertArrayEquals(new int[] {2, 1}, ListNode.toArray(Lists.reverse(ListNode.of(1, 2))));
    }

    @Test void reverseBetweenExamples() {
        assertArrayEquals(new int[] {1, 4, 3, 2, 5}, ListNode.toArray(Lists.reverseBetween(ListNode.of(1, 2, 3, 4, 5), 2, 4)));
        assertArrayEquals(new int[] {5, 4, 3, 2, 1}, ListNode.toArray(Lists.reverseBetween(ListNode.of(1, 2, 3, 4, 5), 1, 5)));
        assertArrayEquals(new int[] {3, 2, 1, 4}, ListNode.toArray(Lists.reverseBetween(ListNode.of(1, 2, 3, 4), 1, 3)));
        assertArrayEquals(new int[] {1, 2, 4, 3}, ListNode.toArray(Lists.reverseBetween(ListNode.of(1, 2, 3, 4), 3, 4)));
        assertArrayEquals(new int[] {1, 2, 3}, ListNode.toArray(Lists.reverseBetween(ListNode.of(1, 2, 3), 2, 2)));
        assertArrayEquals(new int[] {9}, ListNode.toArray(Lists.reverseBetween(ListNode.of(9), 1, 1)));
    }

    @Test void reverseBetweenMatchesArrayReference() {
        Random r = new Random(5);
        for (int it = 0; it < 500; it++) {
            int n = 1 + r.nextInt(12);
            int[] a = r.ints(n, -5, 6).toArray();
            int l = 1 + r.nextInt(n), rr = l + r.nextInt(n - l + 1);
            int[] expected = a.clone();
            for (int i = l - 1, j = rr - 1; i < j; i++, j--) { int t = expected[i]; expected[i] = expected[j]; expected[j] = t; }
            assertArrayEquals(expected, ListNode.toArray(Lists.reverseBetween(ListNode.of(a), l, rr)));
        }
    }

    @Test void palindromeExamples() {
        assertTrue(Lists.isPalindrome(null));
        assertTrue(Lists.isPalindrome(ListNode.of(1)));
        assertTrue(Lists.isPalindrome(ListNode.of(1, 2, 2, 1)));
        assertTrue(Lists.isPalindrome(ListNode.of(1, 2, 3, 2, 1)));
        assertFalse(Lists.isPalindrome(ListNode.of(1, 2)));
        assertFalse(Lists.isPalindrome(ListNode.of(1, 2, 3, 1)));
        assertTrue(Lists.isPalindrome(ListNode.of(7, 7, 7)));
    }

    @Test void palindromeRandomAgainstArray() {
        Random r = new Random(6);
        for (int it = 0; it < 2000; it++) {
            int[] a = r.ints(r.nextInt(9), 0, 3).toArray();
            boolean expected = true;
            for (int i = 0, j = a.length - 1; i < j; i++, j--) expected &= a[i] == a[j];
            assertEquals(expected, Lists.isPalindrome(ListNode.of(a)), java.util.Arrays.toString(a));
        }
    }

    @Test @Timeout(3) void longListsNoStackOverflow() {
        int n = 1_000_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i % 1000;
        ListNode rev = Lists.reverse(ListNode.of(a));
        assertEquals(a[n - 1], rev.val);
        int[] p = new int[n];
        for (int i = 0; i < n; i++) p[i] = Math.min(i, n - 1 - i);
        assertTrue(Lists.isPalindrome(ListNode.of(p)));
        p[n / 2 + 7]++;
        assertFalse(Lists.isPalindrome(ListNode.of(p)));
    }
}
