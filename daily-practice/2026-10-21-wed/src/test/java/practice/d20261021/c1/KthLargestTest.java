package practice.d20261021.c1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class KthLargestTest {

    @Test @DisplayName("examples from the question")
    void examples() {
        assertEquals(5, KthLargest.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
        assertEquals(4, KthLargest.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }

    @Test @DisplayName("single element, k = 1 and k = n")
    void edges() {
        assertEquals(1, KthLargest.findKthLargest(new int[]{1}, 1));
        assertEquals(9, KthLargest.findKthLargest(new int[]{4, 9, 2}, 1));
        assertEquals(2, KthLargest.findKthLargest(new int[]{4, 9, 2}, 3));
    }

    @Test @DisplayName("duplicates and negatives")
    void duplicatesAndNegatives() {
        assertEquals(7, KthLargest.findKthLargest(new int[]{7, 7, 7, 7}, 3));
        assertEquals(-2, KthLargest.findKthLargest(new int[]{-1, -2, -3, -2}, 2));
        assertEquals(Integer.MIN_VALUE, KthLargest.findKthLargest(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, 2));
    }

    @Test @DisplayName("random arrays match sort-and-index")
    void randomAgainstSort() {
        Random rnd = new Random(7);
        for (int t = 0; t < 3000; t++) {
            int n = rnd.nextInt(15) + 1; int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(9) - 4;
            int k = rnd.nextInt(n) + 1; int[] s = a.clone(); Arrays.sort(s);
            assertEquals(s[n - k], KthLargest.findKthLargest(a.clone(), k), Arrays.toString(a) + " k=" + k);
        }
    }

    @Test @DisplayName("200,000 equal values finish fast (catches a 2-way partition going quadratic)")
    void allEqualIsFast() {
        int[] a = new int[200_000]; Arrays.fill(a, 5);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(5, KthLargest.findKthLargest(a, 100_000)));
    }

    @Test @DisplayName("2,000,000 sorted values finish fast (catches a fixed first/last pivot)")
    void sortedIsFast() {
        int[] a = new int[2_000_000]; for (int i = 0; i < a.length; i++) a[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(1_999_990, KthLargest.findKthLargest(a, 10)));
    }
}
