package practice.e;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/** Stateless and thread-safe (validators are cached). The pattern is compiled once; Java's \d is ASCII-only unless UNICODE_CHARACTER_CLASS is set. */
public class SerialValidator implements ConstraintValidator<ValidSerial, String> {
    private static final Pattern FORMAT = Pattern.compile("[A-Z]{2}-\\d{6}");

    @Override public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        if (!FORMAT.matcher(value).matches()) return false;
        int sum = 0;
        for (int i = 3; i < 8; i++) sum += value.charAt(i) - '0';
        return sum % 10 == value.charAt(8) - '0';
    }
}
