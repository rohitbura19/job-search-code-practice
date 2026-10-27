package practice.e;

// TODO: turn this into a Bean Validation constraint annotation:
//   - @Constraint(validatedBy = SerialValidator.class), runtime retention, targets: field and parameter, @Documented
//   - the three mandatory members: message (give it a helpful default), groups, payload
public @interface ValidSerial {
}
