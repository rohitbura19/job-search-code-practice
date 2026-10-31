package practice.e;

import org.springframework.http.ResponseEntity;

/**
 * TODO: REST controller under /api/notes (add the class-level and method-level annotations).
 *   POST   /                  body NoteRequest (validated) -> 201 + Location: /api/notes/{id} + NoteView
 *   GET    /{id}              -> NoteView
 *   GET    /?owner&q&page&size   owner required; q optional; page >= 0 (default 0); size >= 1 (default 20) -> PagedResponse; invalid -> 400
 *   PUT    /{id}              body NoteUpdate (validated) -> NoteView
 *   DELETE /{id}              -> 204 no content
 *   POST   /{id}/restore      -> NoteView
 */
public class NoteController {
    private final NoteService service;

    public NoteController(NoteService service) { this.service = service; }

    public ResponseEntity<NoteView> create(NoteRequest req) { throw new UnsupportedOperationException("TODO"); }
    public NoteView get(long id) { throw new UnsupportedOperationException("TODO"); }
    public PagedResponse<NoteView> list(String owner, String q, int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public NoteView update(long id, NoteUpdate req) { throw new UnsupportedOperationException("TODO"); }
    public ResponseEntity<Void> delete(long id) { throw new UnsupportedOperationException("TODO"); }
    public NoteView restore(long id) { throw new UnsupportedOperationException("TODO"); }
}
