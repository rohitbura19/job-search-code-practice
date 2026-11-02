package practice.e;

import java.math.BigDecimal;

// TODO: fromId and toId required; amount required, at least 0.01, at most 12 integer and 2 fraction digits. Violations -> 400.
public record TransferRequest(Long fromId, Long toId, BigDecimal amount) {}
