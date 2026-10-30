package practice.d20261030.h2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class GenericsTest {
    @Test void maxWorksForPlainAndInheritedComparables() {
        assertEquals(9, Generics.max(List.of(3, 9, 4)));
        assertEquals("pear", Generics.max(Set.of("fig", "pear", "kiwi")));
        List<Timestamp> ts = List.of(new Timestamp(5), new Timestamp(50), new Timestamp(7));
        Timestamp latest = Generics.max(ts);          // needs Comparable<? super T>
        assertEquals(50, latest.getTime());
        assertEquals(1, Generics.max(List.of(1)));
        assertThrows(NoSuchElementException.class, () -> Generics.max(new TreeSet<Integer>()));
    }

    @Test void copyAcceptsWiderDestinationAndNarrowerSource() {
        List<Number> numbers = new ArrayList<>(List.of(1.5));
        Generics.copy(numbers, List.of(1, 2));
        assertEquals(List.of(1.5, 1, 2), numbers);
        List<Object> objects = new ArrayList<>();
        Generics.copy(objects, List.of("a", "b"));
        Generics.<Object>copy(objects, List.of(3));
        assertEquals(List.of("a", "b", 3), objects);
        List<Integer> same = new ArrayList<>();
        Generics.copy(same, same);                    // empty onto itself: no infinite loop
        assertEquals(List.of(), same);
    }

    @Test void argMax() {
        Map<String, Integer> m = new LinkedHashMap<>();
        assertEquals(Optional.empty(), Generics.argMax(m));
        m.put("a", 3); m.put("b", 7); m.put("c", 7); m.put("d", 1);
        assertEquals(Optional.of("b"), Generics.argMax(m));            // tie -> first
        Map<Integer, Timestamp> byTime = Map.of(1, new Timestamp(10));
        assertEquals(Optional.of(1), Generics.argMax(byTime));
    }

    @Test void sum() {
        assertEquals(6.0, Generics.sum(List.of(1, 2, 3)), 0);
        assertEquals(3.75, Generics.sum(Set.of(1.5, 2.25)), 0);
        List<? extends Number> mixed = List.of(1, 2.5, 3L);
        assertEquals(6.5, Generics.sum(mixed), 0);
        assertEquals(0.0, Generics.sum(List.of()), 0);
    }

    @Test void reverseInPlaceOnWildcardList() {
        List<String> l = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
        Generics.reverseInPlace(l);
        assertEquals(List.of("e", "d", "c", "b", "a"), l);
        List<Integer> even = new ArrayList<>(List.of(1, 2));
        Generics.reverseInPlace(even);
        assertEquals(List.of(2, 1), even);
        Generics.reverseInPlace(new ArrayList<Object>());
        List<?> unknown = new ArrayList<>(List.of(1, "x"));
        Generics.reverseInPlace(unknown);
        assertEquals(List.of("x", 1), unknown);
    }

    @Test void registryExactTypeLookup() {
        TypedRegistry r = new TypedRegistry();
        r.put(String.class, "hello");
        r.put(Number.class, 42);
        assertEquals("hello", r.get(String.class));
        assertEquals(42, r.get(Number.class));
        assertEquals(null, r.get(Integer.class));                 // exact class only
        assertEquals(null, r.get(Object.class));
        r.put(String.class, "bye");
        assertEquals("bye", r.get(String.class));
    }

    @Test void registryPrimitivesShareWrapperEntry() {
        TypedRegistry r = new TypedRegistry();
        r.put(int.class, 5);
        assertEquals(5, r.get(Integer.class));
        assertEquals(5, r.get(int.class));
        r.put(Boolean.class, true);
        assertEquals(true, r.get(boolean.class));
    }

    @Test @SuppressWarnings({"unchecked", "rawtypes"}) void registryRejectsBadInput() {
        TypedRegistry r = new TypedRegistry();
        assertThrows(NullPointerException.class, () -> r.put(String.class, null));
        assertThrows(NullPointerException.class, () -> r.put(null, "x"));
        assertThrows(NullPointerException.class, () -> r.get(null));
        Class raw = String.class;
        assertThrows(ClassCastException.class, () -> r.put(raw, Integer.valueOf(1)));    // heap pollution caught at the door
        assertEquals(null, r.get(String.class));
    }

    @Test void getOrCreateCallsFactoryOnce() {
        TypedRegistry r = new TypedRegistry();
        int[] calls = {0};
        List<String> first = r.getOrCreate(List.class, () -> { calls[0]++; return new ArrayList<String>(); });
        List<?> second = r.getOrCreate(List.class, () -> { calls[0]++; return new ArrayList<>(); });
        assertSame(first, second);
        assertEquals(1, calls[0]);
        assertThrows(NullPointerException.class, () -> r.getOrCreate(Map.class, () -> null));
    }
}
