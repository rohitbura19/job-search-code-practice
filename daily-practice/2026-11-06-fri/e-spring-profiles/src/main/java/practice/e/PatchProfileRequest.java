package practice.e;

// TODO(E4): null means "leave unchanged", so absent fields must pass validation, but a PRESENT displayName must have 1..50 chars
//  and a present city at least 1 char (use @Size, which ignores null).
public record PatchProfileRequest(String displayName, String city) {}
