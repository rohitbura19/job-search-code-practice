package practice.e;

// TODO: Bean Validation on the components: paymentId not blank, at most 40 chars; amountPaise not null and positive;
//       reason optional but at most 200 chars. Why Long (not long)?
public record CreateRefundRequest(String paymentId, Long amountPaise, String reason) {}
