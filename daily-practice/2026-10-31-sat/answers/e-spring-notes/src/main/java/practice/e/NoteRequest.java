package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NoteRequest(
        @NotBlank @Pattern(regexp = "[a-z0-9_]{3,20}") String owner,
        @NotBlank @Size(max = 100) String title,
        @Size(max = 2000) String body) {}
