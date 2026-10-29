package practice.e;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Bound from `reviews.max-page-size` in application.yml (relaxed binding). */
@ConfigurationProperties(prefix = "reviews")
public record ReviewProps(int maxPageSize) {}
