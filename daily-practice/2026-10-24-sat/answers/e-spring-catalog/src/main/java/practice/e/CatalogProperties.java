package practice.e;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/** Bound from `catalog.*` (relaxed binding: catalog.max-page-size, CATALOG_MAXPAGESIZE). @Validated fails the startup on bad values. */
@ConfigurationProperties(prefix = "catalog")
@Validated
public record CatalogProperties(@DefaultValue("20") @Min(1) int defaultPageSize, @DefaultValue("100") @Min(1) int maxPageSize) {}
