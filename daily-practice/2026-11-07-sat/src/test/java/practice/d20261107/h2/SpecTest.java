package practice.d20261107.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class SpecTest {
    record User(String name, int age, String country, boolean banned) {}

    static final Spec<User> ADULT = Spec.where("adult", User::age, a -> a >= 18);
    static final Spec<User> BANNED = Spec.where("banned", User::banned, b -> b);
    static final Spec<User> IN = Spec.where("in", User::country, "IN"::equals);
    static final User ASHA = new User("asha", 30, "IN", false);
    static final User KID = new User("kid", 9, "IN", false);
    static final User BOB = new User("bob", 40, "US", true);

    @Test void basicComposition() {
        Spec<User> s = ADULT.and(BANNED.not());
        assertTrue(s.isSatisfiedBy(ASHA));
        assertFalse(s.isSatisfiedBy(KID));
        assertFalse(s.isSatisfiedBy(BOB));
        assertEquals("(adult AND NOT banned)", s.describe());
        assertEquals("(adult OR in)", ADULT.or(IN).describe());
        assertEquals("NOT (adult OR in)", ADULT.or(IN).not().describe());
        assertEquals("NOT NOT adult", ADULT.not().not().describe());
        assertTrue(ADULT.not().not().isSatisfiedBy(ASHA));
    }

    @Test void operandsAreNotMutated() {
        Spec<User> before = ADULT;
        ADULT.and(BANNED);
        ADULT.or(BANNED);
        ADULT.not();
        assertSame(before, ADULT);
        assertTrue(ADULT.isSatisfiedBy(BOB));
        assertEquals("adult", ADULT.describe());
    }

    @Test void shortCircuit() {
        AtomicInteger calls = new AtomicInteger();
        Spec<User> counting = Spec.of("counting", u -> { calls.incrementAndGet(); return true; });
        assertFalse(ADULT.and(counting).isSatisfiedBy(KID));
        assertEquals(0, calls.get());
        assertTrue(ADULT.or(counting).isSatisfiedBy(ASHA));
        assertEquals(0, calls.get());
        assertTrue(KID.age() < 18 && ADULT.not().and(counting).isSatisfiedBy(KID));
        assertEquals(1, calls.get());
    }

    @Test void allOfAnyOf() {
        assertTrue(Spec.<User>allOf().isSatisfiedBy(ASHA));
        assertFalse(Spec.<User>anyOf().isSatisfiedBy(ASHA));
        assertEquals("TRUE", Spec.<User>allOf().describe());
        assertEquals("FALSE", Spec.<User>anyOf().describe());
        Spec<User> all = Spec.allOf(ADULT, IN, BANNED.not());
        assertTrue(all.isSatisfiedBy(ASHA));
        assertFalse(all.isSatisfiedBy(BOB));
        assertEquals("((adult AND in) AND NOT banned)", all.describe());
        Spec<User> any = Spec.anyOf(BANNED, IN);
        assertTrue(any.isSatisfiedBy(KID));
        assertEquals("(banned OR in)", any.describe());
        assertEquals("adult", Spec.<User>allOf(ADULT).describe());
    }

    @Test void allOfCopiesItsArguments() {
        @SuppressWarnings("unchecked") Spec<User>[] arr = new Spec[]{ADULT, IN};
        Spec<User> s = Spec.allOf(arr);
        arr[0] = BANNED;
        assertEquals("(adult AND in)", s.describe());
    }

    @Test void contravariantOperands() {
        Spec<Object> notNull = Spec.of("nonNull", o -> o != null);
        Spec<String> longish = Spec.where("long", String::length, n -> n > 3);
        Spec<String> both = longish.and(notNull);
        assertTrue(both.isSatisfiedBy("hello"));
        assertFalse(both.isSatisfiedBy("hi"));
        Spec<String> viaAllOf = Spec.allOf(notNull, longish);
        assertTrue(viaAllOf.isSatisfiedBy("hello"));
    }

    @Test void whereWithNullProperty() {
        Spec<String> isNullLength = Spec.where("noName", s -> s.isEmpty() ? null : s, v -> v == null);
        assertTrue(isNullLength.isSatisfiedBy(""));
        assertFalse(isNullLength.isSatisfiedBy("x"));
    }

    @Test void failFastOnNulls() {
        assertThrows(NullPointerException.class, () -> ADULT.and(null));
        assertThrows(NullPointerException.class, () -> ADULT.or(null));
        assertThrows(NullPointerException.class, () -> Spec.<User>allOf(ADULT, null));
        assertThrows(NullPointerException.class, () -> Spec.<User>anyOf((Spec<User>) null));
        assertThrows(NullPointerException.class, () -> Spec.where("x", null, v -> true));
        assertThrows(NullPointerException.class, () -> Spec.of("x", null));
    }

    @Test void deepChainsEvaluateWithoutOverflow() {
        Spec<User> s = ADULT;
        for (int i = 0; i < 500; i++) s = s.and(IN.or(BANNED));
        assertTrue(s.isSatisfiedBy(ASHA));
        assertFalse(s.isSatisfiedBy(KID));
    }
}
