package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(@NotBlank @Size(max = 50) String author, @NotBlank @Size(max = 500) String text) {}
