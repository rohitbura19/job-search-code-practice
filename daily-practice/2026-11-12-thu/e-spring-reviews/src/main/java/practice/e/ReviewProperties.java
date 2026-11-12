package practice.e;

import java.util.List;

// TODO(E3): bind the `reviews.*` properties of application.yml: @ConfigurationProperties(prefix = "reviews") + @Validated;
//  bannedWords @NotNull; maxPageSize defaults to 20 via @DefaultValue and must be 1..100 (@Min/@Max).
//  (ReviewsApplication already has @ConfigurationPropertiesScan.)
public record ReviewProperties(List<String> bannedWords, int maxPageSize) {}
