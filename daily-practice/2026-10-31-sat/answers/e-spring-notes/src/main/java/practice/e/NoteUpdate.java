package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteUpdate(@NotBlank @Size(max = 100) String title, @Size(max = 2000) String body, @NotNull Long version) {}
