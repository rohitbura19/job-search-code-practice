package practice.e;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReview(@Min(1) @Max(5) int rating, @NotBlank @Size(max = 80) String title, @Size(max = 2000) String body) {}
