package practice.d20261101.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class SemverTest {
    static Semver v(String s) { return Semver.parse(s); }

    @Test void parseAndAccessors() {
        Semver s = v("1.20.3-alpha.1+build.5");
        assertEquals(1, s.major());
        assertEquals(20, s.minor());
        assertEquals(3, s.patch());
        assertEquals("alpha.1", s.preRelease());
        assertEquals("1.20.3-alpha.1", s.toString());
        assertEquals("", v("0.0.0").preRelease());
        assertEquals("0.0.0", v("0.0.0").toString());
        assertEquals("1.0.0-0A.is.legal", v("1.0.0-0A.is.legal").toString());
        assertEquals("1.0.0-x-y", v("1.0.0-x-y").toString());     // only the first '-' starts the pre-release
    }

    @Test void parseRejectsGarbage() {
        for (String bad : new String[] {"", "1", "1.2", "1.2.3.4", "01.2.3", "1.02.3", "1.2.03", "1.2.3-", "1.2.3-01", "1.2.3-a..b", "a.b.c",
                "-1.2.3", "1.2.3+", "1.2.3+a..b", "1.2.3 ", " 1.2.3", "99999999999.0.0", "1.2.3-é", "1.2.3+é", "v1.2.3"})
            assertThrows(IllegalArgumentException.class, () -> Semver.parse(bad), "should reject '" + bad + "'");
        assertThrows(IllegalArgumentException.class, () -> Semver.parse(null));
    }

    @Test void specOrdering() {
        List<String> ordered = List.of("1.0.0-alpha", "1.0.0-alpha.1", "1.0.0-alpha.beta", "1.0.0-beta", "1.0.0-beta.2", "1.0.0-beta.11",
                "1.0.0-rc.1", "1.0.0", "1.0.1", "1.1.0", "2.0.0", "10.0.0");
        for (int i = 0; i < ordered.size(); i++)
            for (int j = 0; j < ordered.size(); j++)
                assertEquals(Integer.signum(Integer.compare(i, j)), Integer.signum(v(ordered.get(i)).compareTo(v(ordered.get(j)))), ordered.get(i) + " vs " + ordered.get(j));
        List<Semver> shuffled = new ArrayList<>(ordered.stream().map(SemverTest::v).toList());
        Collections.shuffle(shuffled, new Random(9));
        Collections.sort(shuffled);
        assertEquals(ordered, shuffled.stream().map(Semver::toString).toList());
    }

    @Test void hugeNumericPreReleaseIdentifiers() {
        assertTrue(v("1.0.0-99999999999999999999").compareTo(v("1.0.0-100000000000000000000")) < 0);
        assertTrue(v("1.0.0-9").compareTo(v("1.0.0-10")) < 0);
        assertTrue(v("1.0.0-9999").compareTo(v("1.0.0-a")) < 0);          // numeric < alphanumeric
    }

    @Test void buildMetadataIsIgnoredEverywhere() {
        assertEquals(v("1.0.0+a"), v("1.0.0+b"));
        assertEquals(0, v("1.0.0+a").compareTo(v("1.0.0")));
        assertEquals(v("1.0.0+a").hashCode(), v("1.0.0+b").hashCode());
        assertEquals(1, new HashSet<>(List.of(v("1.0.0+a"), v("1.0.0+b"), v("1.0.0"))).size());
    }

    @Test void equalsHashCodeAndTreeSetAgree() {
        Random r = new Random(5);
        String[] pool = {"1.0.0", "1.0.0-a", "1.0.0-a.1", "1.0.0-1", "1.0.1", "0.9.9", "1.0.0-a+x", "1.0.0-b", "2.0.0-rc.1"};
        List<Semver> all = Stream.generate(() -> v(pool[r.nextInt(pool.length)])).limit(200).toList();
        assertEquals(new HashSet<>(all).size(), new TreeSet<>(all).size());
        for (Semver a : all) for (Semver b : all) {
            assertEquals(a.equals(b), a.compareTo(b) == 0);
            assertEquals(-Integer.signum(a.compareTo(b)), Integer.signum(b.compareTo(a)));
            if (a.equals(b)) assertEquals(a.hashCode(), b.hashCode());
        }
        assertNotEquals(v("1.0.0"), null);
        assertNotEquals(v("1.0.0"), "1.0.0");
    }

    @Test void bumpsClearLowerPartsAndPreRelease() {
        assertEquals(v("2.0.0"), v("1.4.5-rc.1").bumpMajor());
        assertEquals(v("1.5.0"), v("1.4.5-rc.1").bumpMinor());
        assertEquals(v("1.4.6"), v("1.4.5-rc.1").bumpPatch());
        Semver orig = v("1.4.5");
        orig.bumpPatch();
        assertEquals("1.4.5", orig.toString());                          // immutable
        assertThrows(ArithmeticException.class, () -> v("2147483647.0.0").bumpMajor());
    }

    @Test void isFinalAndFieldsAreFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(Semver.class.getModifiers()));
        for (var f : Semver.class.getDeclaredFields())
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) assertTrue(java.lang.reflect.Modifier.isFinal(f.getModifiers()), f.getName());
    }
}
