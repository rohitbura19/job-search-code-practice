package practice.e;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** Stateless and thread-safe (one instance is shared). Counts letters and lower-case letters by code point; null is left to @NotNull. */
public class NotAllCapsValidator implements ConstraintValidator<NotAllCaps, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        int letters = 0;
        for (int i = 0; i < value.length(); ) {
            int cp = value.codePointAt(i);
            i += Character.charCount(cp);
            if (Character.isLowerCase(cp)) return true;
            if (Character.isLetter(cp)) letters++;
        }
        return letters < 6;
    }
}
