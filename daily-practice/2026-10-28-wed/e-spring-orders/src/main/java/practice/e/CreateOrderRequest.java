package practice.e;

import java.util.List;

// TODO validation: orderNo matches ORD- plus six digits (and is not blank); customerId not blank, at most 40 chars;
//       lines not empty, at most 50, and EACH line validated (cascade into the elements).
public record CreateOrderRequest(String orderNo, String customerId, List<LineRequest> lines) {}
