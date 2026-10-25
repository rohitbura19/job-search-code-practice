package practice.e;

import java.time.LocalDate;

// TODO: Bean Validation on the components: code not blank and matching [A-Z0-9]{4,30}; percentOff not null, 1..100;
//       validUntil not null and in the FUTURE (@Future); maxRedemptions not null and positive. Why Integer (not int)?
public record CreateCouponRequest(String code, Integer percentOff, LocalDate validUntil, Integer maxRedemptions) {}
