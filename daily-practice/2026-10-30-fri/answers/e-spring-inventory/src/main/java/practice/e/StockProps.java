package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "stock")
public record StockProps(
        @DefaultValue("5") @Min(0) int lowThreshold,
        @DefaultValue("50") @Min(1) int maxPageSize,
        @DefaultValue @Valid Topics topics) {

    public record Topics(
            @DefaultValue("stock.reserved") @NotBlank String reserved,
            @DefaultValue("stock.low") @NotBlank String lowStock,
            @DefaultValue("stock.restock") @NotBlank String restock) {}
}
