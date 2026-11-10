package practice.e;

/**
 * TODO(E3): Bean Validation.
 *  - email: not blank, a well-formed e-mail, at most 120 characters.
 *  - plan: required.
 * ChangePlanRequest.plan: required.
 */
public record CreateSubscriptionRequest(String email, Plan plan) {
    public record ChangePlanRequest(Plan plan) {}
}
