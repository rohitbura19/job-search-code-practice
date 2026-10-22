package practice.e.estimate;

import java.math.BigDecimal;

/** GIVEN. */
public record EstimateResponse(String siteId, BigDecimal cost, String currency, boolean fxFallbackUsed) {}
