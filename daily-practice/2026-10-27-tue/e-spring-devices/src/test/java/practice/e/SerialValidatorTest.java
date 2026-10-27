package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SerialValidatorTest {
    final SerialValidator v = new SerialValidator();

    @ParameterizedTest
    @ValueSource(strings = {"AB-123455", "ZZ-000000", "QX-999995", "EF-222220", "GH-555555"})
    void acceptsWellFormedSerialsWithTheRightCheckDigit(String s) { assertTrue(v.isValid(s, null), s); }

    @ParameterizedTest
    @ValueSource(strings = {"AB-123456", "ab-123455", "AB-12345", "AB123455", "AB-1234555", "", " ", "AB-12345٥", "A1-123455", "AB-123455 "})
    void rejectsBadCheckDigitFormatCaseAndNonAsciiDigits(String s) { assertFalse(v.isValid(s, null), s); }

    @Test void nullIsValidSoThatNotNullDecidesRequiredness() { assertTrue(v.isValid(null, null)); }

    @Test void itIsAWiredConstraintAnnotation() {
        assertTrue(ValidSerial.class.isAnnotationPresent(jakarta.validation.Constraint.class));
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME, ValidSerial.class.getAnnotation(java.lang.annotation.Retention.class).value());
        assertTrue(SerialValidator.class.getInterfaces().length == 1);
    }
}
