package practice.e;

import java.math.BigDecimal;

// TODO: owner not blank, at most 40 chars. openingBalance optional, but if present zero or positive, at most 12 integer and 2 fraction digits.
public record AccountRequest(String owner, BigDecimal openingBalance) {}
