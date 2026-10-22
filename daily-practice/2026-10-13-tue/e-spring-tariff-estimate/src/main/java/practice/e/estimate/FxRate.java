package practice.e.estimate;

import java.math.BigDecimal;

/** GIVEN - JSON body of the FX service: {"rate": 0.012}. */
public record FxRate(BigDecimal rate) {}
