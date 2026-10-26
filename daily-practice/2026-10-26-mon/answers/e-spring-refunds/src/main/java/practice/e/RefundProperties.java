package practice.e;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "refund")
@Validated
public record RefundProperties(@DefaultValue("5000000") @Min(1) long maxRefundPaise,
                               @DefaultValue("100000") @Min(0) long autoApproveUpToPaise) {
    // Cross-field rule: a getter-style boolean method annotated @AssertTrue is validated like a property.
    @AssertTrue(message = "refund.auto-approve-up-to-paise must not exceed refund.max-refund-paise")
    public boolean isThresholdWithinMax() { return autoApproveUpToPaise <= maxRefundPaise; }
}
