package practice.e;

import jakarta.validation.ConstraintValidatorContext;

// TODO: implement ConstraintValidator<ValidSerial, String>. A serial is two capital letters, a dash and six digits, e.g. AB-123455,
//   where the LAST digit is the check digit: the sum of the five digits before it, modulo 10 (1+2+3+4+5 = 15 -> 5).
//   null is VALID (combine with @NotNull when the field is required). Be careful: \d must not accept non-ASCII digits.
public class SerialValidator {
    public boolean isValid(String value, ConstraintValidatorContext context) { throw new UnsupportedOperationException("TODO"); }
}
