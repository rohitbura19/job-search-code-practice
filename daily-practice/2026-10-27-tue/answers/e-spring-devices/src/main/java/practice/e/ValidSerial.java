package practice.e;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = SerialValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSerial {
    String message() default "must look like AB-123455: two capitals, a dash, six digits, the last one a valid check digit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
