package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SlotService {
    private final SlotRepository repo;

    public SlotService(SlotRepository repo) { this.repo = repo; }

    @Transactional
    public SlotView create(CreateSlotRequest r) {
        return SlotView.of(repo.save(new Slot(r.stationId(), r.startsAt(), r.capacity())));
    }

    @Transactional(readOnly = true)
    public List<SlotView> available(String stationId, Instant from) {
        return repo.available(stationId, from).stream().map(SlotView::of).toList();
    }

    @Transactional
    public SlotView reserve(long id) {
        if (repo.reserve(id) == 0) {
            if (!repo.existsById(id)) throw new SlotNotFoundException(id);
            throw new SlotFullException(id);
        }
        return SlotView.of(repo.findById(id).orElseThrow());
    }
}
