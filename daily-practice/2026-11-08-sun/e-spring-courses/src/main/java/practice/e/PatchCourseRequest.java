package practice.e;

import jakarta.validation.constraints.*;

/** PATCH semantics: null = "leave unchanged". */
public record PatchCourseRequest(
        @Size(max = 100) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String title,
        @Min(1) @Max(500) Integer capacity) {}
