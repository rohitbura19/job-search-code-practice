package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(@NotBlank @Size(max = 100) String title) {}
