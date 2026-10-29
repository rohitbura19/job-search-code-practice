package practice.d20261029.h1;

/**
 * Why each result (JLS section in brackets):
 * s1: `t ? 1 : "x"` is a reference conditional -> Integer 1. `t ? 1 : 2.0` is numeric: binary promotion to double -> 1.0 [15.25].
 *     Two boxed operands Integer/Double are unboxed and promoted too -> 1.0 (the classic trap). `'a' : 0`: 0 is a constant that fits
 *     char -> type char -> a. `'b' : i` with a non-constant int -> int -> 98. `!t ? 1 : nul` mixes int and Integer -> type int, so the taken operand `nul` is unboxed -> NPE. `t ? nul : 0` likewise -> NPE2.
 * s2: 127 is cached (a == b), 128 is not (c == d false); equals on the same type is value equality; Long.equals(Integer) is false
 *     (type is part of equality); set holds Integer, Long, Short, Byte = 4; m.get(1) looks up an Integer key in a Long map -> null;
 *     c <= d and c >= d unbox, while c != d compares references.
 * s3: phase 1 allows widening only: 1 -> int; byte, char, short -> int (widening to int beats long and double); float -> double;
 *     Integer -> Integer; "s" -> String (most specific); (Object) -> Object; no args and (1, 2) -> varargs (phase 3);
 *     1L -> long. wide(5): phase 1 widening int -> long wins over boxing; wide((short) 5): short widens to long too.
 * s4: resources open in order, close in REVERSE order after the body, before catch and finally; the body exception is primary and the
 *     two close failures are suppressed in close order (b then a). `return x` in tricky() evaluates x before finally runs -> 1;
 *     a `return` in finally discards the exception in swallow() -> 9.
 * s5: remove(1) is by index (removes 20), remove(Integer.valueOf(1)) by value -> [10, 30, 2]. Arrays.asList is fixed-size but writable:
 *     add -> UOE; List.of is immutable: set -> UOE2. A structural change of the base after creating a subList makes the view fail with
 *     CME; base saw the sub's writes: [1, 20, 4, 5, 6]. Arrays.asList(int[]) is a List<int[]> of size 1; with boxed varargs it is 3.
 * s6: changing a key's hashCode after insertion loses the entry for lookups by either the old or the new value (null, null, false)
 *     but size stays 1. The case-insensitive TreeSet treats Apple and apple as the same element (keeps the first) and contains("APPLE")
 *     uses the comparator; a HashSet copy uses equals/hashCode -> false. Iterator.remove is safe -> [1, 3, 5]. Removing the second-to-last
 *     element inside a for-each hides the CME because hasNext() sees cursor == size -> [2].
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "1 1.0 1.0 a 98 NPE NPE2"; }
    public static String s2() { return "true false true false true true 4 null true"; }
    public static String s3() { return "int int int int double Integer String Object varargs varargs long long long"; }
    public static String s4() { return "open-a open-b body close-b close-a finally fin | body-fail suppressed=2:close-b,close-a | 1 9"; }
    public static String s5() { return "[10, 30, 2] UOE UOE2 CME [1, 20, 4, 5, 6] 1 3"; }
    public static String s6() { return "null null false 1 [Apple, BANANA] true false [1, 3, 5] [2]"; }
}
