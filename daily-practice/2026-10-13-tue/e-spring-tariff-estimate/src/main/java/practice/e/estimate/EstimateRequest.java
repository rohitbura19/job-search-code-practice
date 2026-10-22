package practice.e.estimate;

import java.math.BigDecimal;

/**
 * TODO - add Bean Validation to the record components:
 *   siteId      not blank
 *   kwh         not null and strictly greater than 0 (@DecimalMin with inclusive = false)
 *   regionCode  not null, matches ^[A-Z]{2}(-[A-Z0-9]{1,3})?$
 *   currency    not null, exactly 3 characters
 */
public record EstimateRequest(String siteId, BigDecimal kwh, String regionCode, String currency) {}
