package practice.e;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Bound from `orders.max-page-size` in application.yml (relaxed binding). */
@ConfigurationProperties(prefix = "orders")
public record OrderProps(int maxPageSize) {}
