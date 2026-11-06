package practice.e;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ETag = the entity version. GET returns it; PATCH must echo it in If-Match (compare-and-swap over HTTP, no lost updates).
 * Missing header -> 428, stale -> 412. Pageable is resolved by Spring Data web support and capped by spring.data.web.pageable.max-page-size.
 */
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ProfileView> create(@Valid @RequestBody CreateProfileRequest r) {
        ProfileView v = service.create(r);
        return ResponseEntity.created(URI.create("/profiles/" + v.id())).eTag(String.valueOf(v.version())).body(v);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileView> get(@PathVariable long id) {
        ProfileView v = service.get(id);
        return ResponseEntity.ok().eTag(String.valueOf(v.version())).body(v);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfileView> patch(@PathVariable long id,
                                             @RequestHeader(value = "If-Match", required = false) String ifMatch,
                                             @Valid @RequestBody PatchProfileRequest r) {
        if (ifMatch == null) throw new Exceptions.PreconditionRequiredException();
        long expected = Long.parseLong(ifMatch.replace("W/", "").replace("\"", "").trim());
        ProfileView v = service.patch(id, expected, r);
        return ResponseEntity.ok().eTag(String.valueOf(v.version())).body(v);
    }

    @GetMapping
    public List<ProfileView> search(@RequestParam(required = false) String city, @RequestParam(required = false) String q, Pageable pageable) {
        return service.search(city, q, pageable).getContent();
    }
}
