package practice.e;

// TODO(E4): add bean-validation annotations: email must be non-blank AND a valid e-mail; displayName non-blank, at most 50 chars;
//  city non-blank; country non-blank and exactly 2 characters (ISO code).
public record CreateProfileRequest(String email, String displayName, String city, String country) {}
