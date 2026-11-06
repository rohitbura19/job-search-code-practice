package practice.d20261106.c1;

import java.util.*;

/**
 * List for O(1) random access + map value -> index. remove swaps the victim with the LAST element, then pops. All O(1) average.
 */
public class RandomizedSet {
    private final List<Integer> items = new ArrayList<>();
    private final Map<Integer, Integer> index = new HashMap<>();
    private final Random random;

    public RandomizedSet(Random random) { this.random = random; }

    public boolean insert(int val) {
        if (index.containsKey(val)) return false;
        index.put(val, items.size());
        items.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = index.remove(val);
        if (i == null) return false;
        int last = items.remove(items.size() - 1);
        if (last != val) {
            items.set(i, last);
            index.put(last, i);
        }
        return true;
    }

    public int getRandom() {
        if (items.isEmpty()) throw new NoSuchElementException();
        return items.get(random.nextInt(items.size()));
    }
}
