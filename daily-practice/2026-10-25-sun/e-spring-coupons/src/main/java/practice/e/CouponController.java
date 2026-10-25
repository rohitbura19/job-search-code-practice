package practice.e;

// TODO: make this a REST controller under /api/coupons; no logic here, only delegation:
//   POST /api/coupons                 valid body -> 201 + Location /api/coupons/{code} + the CouponView
//   POST /api/coupons/{code}/redeem   -> 200 + the CouponView (the service raises 404 / 410 / 409 as CouponException)
//   GET  /api/coupons/{code}          -> 200 + the CouponView
//   The {code} path variable must match [A-Z0-9]{4,30}; otherwise 400 problem+json and the service is never called.
//   Do NOT put @Validated on the class: in Spring 6.1 MVC validates the parameter constraints itself (which exception?).
public class CouponController {
    private final CouponService service;

    public CouponController(CouponService service) { this.service = service; }

    public Object create(CreateCouponRequest request) { return null; }

    public CouponView redeem(String code) { return null; }

    public CouponView get(String code) { return null; }
}
