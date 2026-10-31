package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {
    private final NoteRepository repo;
    private final NotesProps props;

    public NoteService(NoteRepository repo, NotesProps props) { this.repo = repo; this.props = props; }

    @Transactional
    public NoteView create(NoteRequest req) {
        if (repo.countByOwner(req.owner()) >= props.maxPerOwner()) throw new QuotaExceededException(req.owner(), props.maxPerOwner());
        return NoteView.of(repo.saveAndFlush(new Note(req.owner(), req.title(), req.body())));
    }

    @Transactional(readOnly = true)
    public NoteView get(long id) { return NoteView.of(load(id)); }

    @Transactional
    public NoteView update(long id, NoteUpdate req) {
        Note n = load(id);
        if (n.getVersion() != req.version()) throw new StaleVersionException(id, req.version(), n.getVersion());
        n.edit(req.title(), req.body());
        return NoteView.of(repo.saveAndFlush(n));
    }

    @Transactional
    public void delete(long id) { repo.delete(load(id)); }

    @Transactional
    public NoteView restore(long id) {
        if (repo.restore(id) == 0) throw new NoteNotFoundException(id);
        return get(id);
    }

    @Transactional(readOnly = true)
    public PagedResponse<NoteView> list(String owner, String q, int page, int size) {
        PageRequest pr = PageRequest.of(page, Math.min(size, props.maxPageSize()), Sort.by("id"));
        Page<Note> p = (q == null || q.isBlank()) ? repo.findByOwner(owner, pr) : repo.search(owner, q.trim(), pr);
        return PagedResponse.of(p.map(NoteView::of));
    }

    private Note load(long id) { return repo.findById(id).orElseThrow(() -> new NoteNotFoundException(id)); }
}
