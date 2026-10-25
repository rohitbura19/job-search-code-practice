package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private static final String CODE = "[A-Z0-9]{4,30}";
    private final CouponService service;

    public CouponController(CouponService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CouponView> create(@Valid @RequestBody CreateCouponRequest request) {
        CouponView v = service.create(request);
        return ResponseEntity.created(URI.create("/api/coupons/" + v.code())).body(v);
    }

    // No @Validated on the class: Spring 6.1 applies the constraint itself and throws HandlerMethodValidationException (a 400 problem).
    @PostMapping("/{code}/redeem")
    public CouponView redeem(@PathVariable @Pattern(regexp = CODE) String code) { return service.redeem(code); }

    @GetMapping("/{code}")
    public CouponView get(@PathVariable @Pattern(regexp = CODE) String code) { return service.get(code); }
}
