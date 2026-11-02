package practice.e;

import java.math.BigDecimal;

// TODO: bind "wallet.max-transfer" (prefix "wallet"), default 10000.00, required and at least 0.01; an invalid value must fail startup.
public record WalletProps(BigDecimal maxTransfer) {}
