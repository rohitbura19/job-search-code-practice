package practice.e;

import java.time.Instant;
import java.util.List;

// TODO: make this a REST controller under /api and complete the endpoints:
//   POST /api/readings                     201 + Location /api/readings/{id} + the saved ReadingView; invalid body -> 400
//   GET  /api/meters/{meterId}/readings?from=&to=   ISO-8601 instants, both required
//   GET  /api/meters/{meterId}/latest?limit=        default 5, allowed 1..100, otherwise 400 (hint: constraint annotations on the
//                                                    parameter; do you need @Validated on the class in Spring 6.1+? what changes?)
//   GET  /api/meters/{meterId}/total       TotalView
public class ReadingController {
    private final MeterReadingRepository repo;

    public ReadingController(MeterReadingRepository repo) { this.repo = repo; }

    public Object create(ReadingRequest request) { return null; }

    public List<ReadingView> range(String meterId, Instant from, Instant to) { return null; }

    public List<ReadingView> latest(String meterId, int limit) { return null; }

    public TotalView total(String meterId) { return null; }
}
