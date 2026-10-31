package practice.e;

/**
 * TODO: @Service with constructor-injected NoteRepository and NotesProps.
 *   create:  if repo.countByOwner(owner) >= props.maxPerOwner() -> QuotaExceededException; otherwise saveAndFlush (so a duplicate
 *            (owner, title) fails here, inside the method) and return the view.
 *   get:     NoteNotFoundException when missing.
 *   update:  load (NotFound), if note.getVersion() != req.version() -> StaleVersionException(id, expected, actual); else edit and saveAndFlush.
 *   delete:  load (NotFound), repo.delete(note).        restore: repo.restore(id) == 0 -> NotFound; then return get(id).
 *   list:    size = min(size, props.maxPageSize()); PageRequest sorted by id ascending; blank or null q -> repo.findByOwner, else repo.search.
 *   Make the writes transactional and the reads read-only.
 */
public class NoteService {
    public NoteService(NoteRepository repo, NotesProps props) {}

    public NoteView create(NoteRequest req) { throw new UnsupportedOperationException("TODO"); }
    public NoteView get(long id) { throw new UnsupportedOperationException("TODO"); }
    public NoteView update(long id, NoteUpdate req) { throw new UnsupportedOperationException("TODO"); }
    public void delete(long id) { throw new UnsupportedOperationException("TODO"); }
    public NoteView restore(long id) { throw new UnsupportedOperationException("TODO"); }
    public PagedResponse<NoteView> list(String owner, String q, int page, int size) { throw new UnsupportedOperationException("TODO"); }
}
