package practice.e;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** RECORD_COMPONENT lets the constraint sit on a record component; the three attributes (message, groups, payload) are mandatory for the Bean Validation provider. */
@Documented
@Constraint(validatedBy = NotAllCapsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAllCaps {
    String message() default "must not be written in capital letters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
