package practice.e;

// TODO: bind `refund.*` to this record: @ConfigurationProperties(prefix = "refund"), validated at startup.
//   maxRefundPaise defaults to 5_000_000 and must be >= 1; autoApproveUpToPaise defaults to 100_000 and must be >= 0.
//   Extra rule: autoApproveUpToPaise must not exceed maxRefundPaise (a cross-field check: which annotation, on which kind of member?).
public record RefundProperties(long maxRefundPaise, long autoApproveUpToPaise) {}
