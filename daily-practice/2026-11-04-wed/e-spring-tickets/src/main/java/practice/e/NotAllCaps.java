package practice.e;

// TODO: a custom Bean Validation constraint annotation.
//  @Documented @Constraint(validatedBy = NotAllCapsValidator.class) @Target({FIELD, PARAMETER, RECORD_COMPONENT}) @Retention(RUNTIME)
//  attributes every constraint needs: String message() default "must not be written in capital letters"; Class<?>[] groups() default {}; Class<? extends Payload>[] payload() default {};
public @interface NotAllCaps {
}
