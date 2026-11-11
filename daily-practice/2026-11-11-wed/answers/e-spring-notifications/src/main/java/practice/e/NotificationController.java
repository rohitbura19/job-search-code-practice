package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Views.NotificationView> send(@Valid @RequestBody SendRequest r) {
        Views.NotificationView v = service.send(r);
        return ResponseEntity.created(URI.create("/api/notifications/" + v.id())).body(v);
    }

    @GetMapping
    public Views.PageView list(@RequestHeader("X-User-Id") @Positive Long userId,
                               @RequestParam(defaultValue = "false") boolean unreadOnly,
                               @RequestParam(defaultValue = "0") @Min(0) int page,
                               @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return service.list(userId, unreadOnly, page, size);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> unreadCount(@RequestHeader("X-User-Id") @Positive Long userId) {
        return Map.of("unread", service.unreadCount(userId));
    }

    @PatchMapping("/{id}/read")
    public Views.NotificationView markRead(@PathVariable @Positive Long id, @RequestHeader("X-User-Id") @Positive Long userId) {
        return service.markRead(id, userId);
    }

    @PostMapping("/read-all")
    public Map<String, Integer> readAll(@RequestHeader("X-User-Id") @Positive Long userId) {
        return Map.of("updated", service.markAllRead(userId));
    }
}
