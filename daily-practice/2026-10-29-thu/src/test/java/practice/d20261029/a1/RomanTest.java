package practice.d20261029.a1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RomanTest {
    @Test void examples() {
        assertEquals(3, Roman.toInt("III"));
        assertEquals(58, Roman.toInt("LVIII"));
        assertEquals(1994, Roman.toInt("MCMXCIV"));
        assertEquals(4, Roman.toInt("IV"));
        assertEquals(3999, Roman.toInt("MMMCMXCIX"));
        assertEquals("MCMXCIV", Roman.toRoman(1994));
        assertEquals("XLIX", Roman.toRoman(49));
        assertEquals("I", Roman.toRoman(1));
        assertEquals("MMMCMXCIX", Roman.toRoman(3999));
    }

    @Test void roundTripAllValues() {
        for (int n = 1; n <= 3999; n++) assertEquals(n, Roman.toInt(Roman.toRoman(n)), "n=" + n);
    }

    @Test void edges() {
        assertThrows(IllegalArgumentException.class, () -> Roman.toRoman(0));
        assertThrows(IllegalArgumentException.class, () -> Roman.toRoman(4000));
        assertThrows(IllegalArgumentException.class, () -> Roman.toRoman(-5));
        assertThrows(IllegalArgumentException.class, () -> Roman.toInt("ABC"));
        assertEquals(0, Roman.toInt(""));
    }
}
