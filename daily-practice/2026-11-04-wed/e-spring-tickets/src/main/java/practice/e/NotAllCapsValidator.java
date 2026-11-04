package practice.e;

import jakarta.validation.ConstraintValidatorContext;

// TODO: make this class implement jakarta.validation.ConstraintValidator<NotAllCaps, String> (add @Override to isValid).
//  null is VALID (combine with @NotNull when needed). A text is INVALID when it has at least 6 letters and none of the letters is lower case
//  ("SERVER IS DOWN" -> invalid, "Fix DB" -> valid, "HELP!" -> valid because it has only 4 letters, "ÄÖÜÄÖÜ" -> invalid: use Character.isLetter / isLowerCase, not a-z).
public class NotAllCapsValidator {
    public boolean isValid(String value, ConstraintValidatorContext context) { throw new UnsupportedOperationException("TODO"); }
}
