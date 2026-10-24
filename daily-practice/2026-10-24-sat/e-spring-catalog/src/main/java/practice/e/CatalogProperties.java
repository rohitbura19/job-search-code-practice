package practice.e;

// TODO: bind `catalog.*` to this record: make it a @ConfigurationProperties(prefix = "catalog") that is validated at startup.
//       defaultPageSize defaults to 20 and maxPageSize to 100 when absent (@DefaultValue); both must be >= 1 (@Min).
//       CatalogApplication already has @EnableConfigurationProperties; why is a record with constructor binding preferable to setters?
public record CatalogProperties(int defaultPageSize, int maxPageSize) {}
