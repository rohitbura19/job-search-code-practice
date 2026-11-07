package practice.d20261107.h2;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Specification pattern: a named, composable business rule over T.
 * Implementors only provide isSatisfiedBy and describe(); everything else is a default or static method to write.
 * Requirements:
 * - and/or short-circuit left to right; not negates. Composition never mutates the operands (they are reusable and may be shared).
 * - describe() is human readable and fully parenthesised for composites: "(adult AND NOT banned)", "(a OR b)".
 * - Generics: and/or accept Spec<? super T> (a Spec<Object> must combine with a Spec<String>); the result is Spec<T>.
 * - allOf() of nothing is always true, anyOf() of nothing is always false; describe() of those is "TRUE" / "FALSE".
 * - where(name, extractor, predicate) builds a Spec<T> on a property; null extracted values are passed to the predicate as is.
 * - null operands: NullPointerException at composition time (fail fast), not at evaluation time.
 */
public interface Spec<T> {
    boolean isSatisfiedBy(T candidate);

    String describe();

    default Spec<T> and(Spec<? super T> other) { throw new UnsupportedOperationException("TODO"); }

    default Spec<T> or(Spec<? super T> other) { throw new UnsupportedOperationException("TODO"); }

    default Spec<T> not() { throw new UnsupportedOperationException("TODO"); }

    @SafeVarargs
    static <T> Spec<T> allOf(Spec<? super T>... specs) { throw new UnsupportedOperationException("TODO"); }

    @SafeVarargs
    static <T> Spec<T> anyOf(Spec<? super T>... specs) { throw new UnsupportedOperationException("TODO"); }

    static <T, V> Spec<T> where(String name, Function<? super T, ? extends V> extractor, Predicate<? super V> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Adapts a plain predicate; describe() returns name. */
    static <T> Spec<T> of(String name, Predicate<? super T> p) { throw new UnsupportedOperationException("TODO"); }
}
