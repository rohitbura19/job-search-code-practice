package practice.e;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

// TODO(E6): make this a REST controller under /profiles (annotations on the class and on every method, validation on bodies).
public class ProfileController {
    public ProfileController(ProfileService service) { throw new UnsupportedOperationException("TODO"); }

    /** POST /profiles -> 201, Location: /profiles/{id}, ETag = the version, body = the view. Invalid body -> 400 (ProblemDetail). */
    public ResponseEntity<ProfileView> create(CreateProfileRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** GET /profiles/{id} -> 200 with ETag = the version. */
    public ResponseEntity<ProfileView> get(long id) { throw new UnsupportedOperationException("TODO"); }

    /**
     * PATCH /profiles/{id} with header If-Match (optional at the framework level; null -> throw PreconditionRequiredException).
     * The header value may be quoted ("3") or weak (W/"3"); parse the number out of it. Returns 200 with the NEW ETag.
     */
    public ResponseEntity<ProfileView> patch(long id, String ifMatch, PatchProfileRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** GET /profiles?city=&q=&page=&size=&sort= -> the content of the page (a JSON array). Both filters optional. */
    public List<ProfileView> search(String city, String q, Pageable pageable) { throw new UnsupportedOperationException("TODO"); }
}
