package practice.e;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: make this a COMPOSED constraint: not blank AND matches [A-Z0-9-]{6,32},
 * reported as ONE violation with the message below. Which three annotations go on this annotation type?
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = {})
public @interface ChargerSerial {
    String message() default "must be 6 to 32 characters: A-Z, 0-9 and '-'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
