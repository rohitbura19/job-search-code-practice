package practice.e;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/** wallet.max-transfer, default 10000.00, must be at least 0.01 (the context must fail to start otherwise). */
@Validated
@ConfigurationProperties(prefix = "wallet")
public record WalletProps(@NotNull @DecimalMin("0.01") @DefaultValue("10000.00") BigDecimal maxTransfer) {}
