package practice.e;

import org.springframework.http.HttpStatus;

/** One exception type for every business failure; the handler turns status + title into a ProblemDetail. */
public class CouponException extends RuntimeException {
    private final HttpStatus status;
    private final String title;

    public CouponException(HttpStatus status, String title, String detail) {
        super(detail);
        this.status = status;
        this.title = title;
    }

    public HttpStatus status() { return status; }
    public String title() { return title; }

    public static CouponException notFound(String code) { return new CouponException(HttpStatus.NOT_FOUND, "Coupon not found", code); }
    public static CouponException duplicate(String code) { return new CouponException(HttpStatus.CONFLICT, "Duplicate code", code); }
    public static CouponException expired(String code) { return new CouponException(HttpStatus.GONE, "Coupon expired", code); }
    public static CouponException exhausted(String code) { return new CouponException(HttpStatus.CONFLICT, "Coupon exhausted", code); }
    public static CouponException tooGenerous(int pct, int max) {
        return new CouponException(HttpStatus.UNPROCESSABLE_ENTITY, "Discount too high", pct + "% exceeds the maximum of " + max + "%");
    }
    public static CouponException tooMany(int n, int cap) {
        return new CouponException(HttpStatus.UNPROCESSABLE_ENTITY, "Too many redemptions", n + " exceeds the cap of " + cap);
    }
}
