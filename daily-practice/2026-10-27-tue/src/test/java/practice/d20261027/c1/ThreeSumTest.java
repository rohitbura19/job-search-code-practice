package practice.d20261027.c1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class ThreeSumTest {
    @Test void example() {
        assertEquals(List.of(List.of(-1, -1, 2), List.of(-1, 0, 1)), ThreeSum.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }

    @Test void emptyShortNoSolution() {
        assertEquals(List.of(), ThreeSum.threeSum(new int[]{}));
        assertEquals(List.of(), ThreeSum.threeSum(new int[]{0, 0}));
        assertEquals(List.of(), ThreeSum.threeSum(new int[]{1, 2, 3}));
        assertEquals(List.of(), ThreeSum.threeSum(new int[]{-3, -2, -1}));
    }

    @Test void duplicatesProduceOneTriplet() {
        assertEquals(List.of(List.of(0, 0, 0)), ThreeSum.threeSum(new int[]{0, 0, 0, 0, 0}));
        assertEquals(List.of(List.of(-2, 1, 1)), ThreeSum.threeSum(new int[]{-2, 1, 1, 1, 1}));
    }

    @Test void inputIsNotMutated() {
        int[] a = {3, -1, 2, -1, 0};
        ThreeSum.threeSum(a);
        assertEquals(List.of(3, -1, 2, -1, 0), java.util.Arrays.stream(a).boxed().toList());
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(11);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(11) - 5;
            TreeSet<List<Integer>> expect = new TreeSet<>((x, y) -> {
                for (int i = 0; i < 3; i++) if (!x.get(i).equals(y.get(i))) return Integer.compare(x.get(i), y.get(i));
                return 0;
            });
            for (int i = 0; i < a.length; i++)
                for (int j = i + 1; j < a.length; j++)
                    for (int k = j + 1; k < a.length; k++)
                        if (a[i] + a[j] + a[k] == 0) {
                            int[] s = {a[i], a[j], a[k]};
                            java.util.Arrays.sort(s);
                            expect.add(List.of(s[0], s[1], s[2]));
                        }
            assertEquals(new ArrayList<>(expect), ThreeSum.threeSum(a));
        }
    }

    @Test void closestExamples() {
        assertEquals(2, ThreeSum.threeSumClosest(new int[]{-1, 2, 1, -4}, 1));
        assertEquals(0, ThreeSum.threeSumClosest(new int[]{0, 0, 0}, 1));
        assertEquals(3, ThreeSum.threeSumClosest(new int[]{1, 1, 1, 0}, 100));
    }

    @Test void closestRandomAgainstBruteForce() {
        Random r = new Random(5);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[3 + r.nextInt(8)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(41) - 20;
            int target = r.nextInt(81) - 40, best = Integer.MAX_VALUE, bestDiff = Integer.MAX_VALUE;
            for (int i = 0; i < a.length; i++)
                for (int j = i + 1; j < a.length; j++)
                    for (int k = j + 1; k < a.length; k++) {
                        int s = a[i] + a[j] + a[k];
                        if (Math.abs(s - target) < bestDiff) { bestDiff = Math.abs(s - target); best = s; }
                    }
            assertEquals(Math.abs(best - target), Math.abs(ThreeSum.threeSumClosest(a, target) - target));
        }
    }

    @Test void largeInput() {
        Random r = new Random(1);
        int[] a = new int[3000];
        for (int i = 0; i < a.length; i++) a[i] = r.nextInt(200_001) - 100_000;
        int[] zeros = new int[3000];
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            ThreeSum.threeSum(a);
            assertEquals(List.of(List.of(0, 0, 0)), ThreeSum.threeSum(zeros));
            ThreeSum.threeSumClosest(a, 12345);
        });
    }
}
