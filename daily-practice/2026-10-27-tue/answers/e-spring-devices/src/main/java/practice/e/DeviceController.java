package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A constraint on a @RequestParam makes Spring 6.1 run built-in method validation for retireStale only, so a bad "days" raises
 * HandlerMethodValidationException (400 via the base handler). The other methods validate the @Valid body the classic way
 * (MethodArgumentNotValidException). An unknown ?status= fails enum conversion: MethodArgumentTypeMismatchException -> 400.
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService service;

    public DeviceController(DeviceService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<DeviceView> register(@Valid @RequestBody RegisterDeviceRequest request) {
        DeviceView v = service.register(request);
        return ResponseEntity.created(URI.create("/api/devices/" + v.serial())).body(v);
    }

    @GetMapping("/{serial}")
    public DeviceView get(@PathVariable String serial) { return service.get(serial); }

    @PatchMapping("/{serial}")
    public DeviceView patch(@PathVariable String serial, @Valid @RequestBody PatchDeviceRequest request) {
        return service.patch(serial, request);
    }

    @GetMapping
    public List<DeviceView> list(@RequestParam(defaultValue = "") String model,
                                 @RequestParam(defaultValue = "ACTIVE") DeviceStatus status) {
        return service.list(model, status);
    }

    @PostMapping("/maintenance/retire-stale")
    public Map<String, Integer> retireStale(@RequestParam(defaultValue = "90") @Positive int days) {
        return Map.of("retired", service.retireStale(days));
    }
}
