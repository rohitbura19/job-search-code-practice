package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record ArticleRequest(
        @NotNull @Size(max = 300) @Pattern(regexp = "https?://.+") String url,
        @NotBlank @Size(max = 120) String title,
        @Size(max = 20) Set<@NotBlank @Size(max = 20) String> tags) {}
