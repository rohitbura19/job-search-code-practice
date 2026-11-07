package practice.d20261107.h2;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Each combinator returns a new immutable Spec (tiny anonymous classes via of-like helpers). Short-circuit comes from && / ||.
 * allOf/anyOf fold to a single composite, copying the varargs array so later caller mutation cannot change the spec.
 * O(k) per evaluation for k leaves; description built lazily so evaluation stays allocation free.
 */
public interface Spec<T> {
    boolean isSatisfiedBy(T candidate);

    String describe();

    default Spec<T> and(Spec<? super T> other) {
        Objects.requireNonNull(other);
        Spec<T> self = this;
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return self.isSatisfiedBy(c) && other.isSatisfiedBy(c); }
            @Override public String describe() { return "(" + self.describe() + " AND " + other.describe() + ")"; }
        };
    }

    default Spec<T> or(Spec<? super T> other) {
        Objects.requireNonNull(other);
        Spec<T> self = this;
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return self.isSatisfiedBy(c) || other.isSatisfiedBy(c); }
            @Override public String describe() { return "(" + self.describe() + " OR " + other.describe() + ")"; }
        };
    }

    default Spec<T> not() {
        Spec<T> self = this;
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return !self.isSatisfiedBy(c); }
            @Override public String describe() { return "NOT " + self.describe(); }
        };
    }

    @SafeVarargs
    static <T> Spec<T> allOf(Spec<? super T>... specs) {
        Spec<T> acc = constant(true);
        boolean first = true;
        for (Spec<? super T> s : specs.clone()) {
            Objects.requireNonNull(s);
            acc = first ? Spec.<T>of(s) : acc.and(s);
            first = false;
        }
        return acc;
    }

    @SafeVarargs
    static <T> Spec<T> anyOf(Spec<? super T>... specs) {
        Spec<T> acc = constant(false);
        boolean first = true;
        for (Spec<? super T> s : specs.clone()) {
            Objects.requireNonNull(s);
            acc = first ? Spec.<T>of(s) : acc.or(s);
            first = false;
        }
        return acc;
    }

    private static <T> Spec<T> of(Spec<? super T> s) {
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return s.isSatisfiedBy(c); }
            @Override public String describe() { return s.describe(); }
        };
    }

    private static <T> Spec<T> constant(boolean v) {
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return v; }
            @Override public String describe() { return v ? "TRUE" : "FALSE"; }
        };
    }

    static <T, V> Spec<T> where(String name, Function<? super T, ? extends V> extractor, Predicate<? super V> predicate) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(extractor);
        Objects.requireNonNull(predicate);
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return predicate.test(extractor.apply(c)); }
            @Override public String describe() { return name; }
        };
    }

    static <T> Spec<T> of(String name, Predicate<? super T> p) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(p);
        return new Spec<>() {
            @Override public boolean isSatisfiedBy(T c) { return p.test(c); }
            @Override public String describe() { return name; }
        };
    }
}
