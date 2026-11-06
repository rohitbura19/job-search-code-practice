package practice.e;

import jakarta.validation.constraints.Size;

/** null = "leave unchanged". @Size does not reject null, so absent fields pass; a present field must be 1..50 chars. */
public record PatchProfileRequest(@Size(min = 1, max = 50) String displayName, @Size(min = 1) String city) {}
