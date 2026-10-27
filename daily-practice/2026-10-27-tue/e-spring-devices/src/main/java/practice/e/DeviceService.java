package practice.e;

import java.time.Clock;
import java.util.List;

// TODO: make this a Spring service and decide the transaction boundaries (class-level @Transactional, which methods can be readOnly?).
//   register:    DeviceConflictException if the serial exists; otherwise save a new ACTIVE device with lastSeen = now (Clock!), return the view.
//                (Two requests can still pass the check together: that race ends in the unique constraint - see ApiExceptionHandler.)
//   get:         the view, or DeviceNotFoundException.
//   patch:       only non-null fields change (model; channel keeps the current firmware version). NO call to save(): the entity is
//                managed, so dirty checking writes it at commit. DeviceNotFoundException if missing.
//   list:        delegate to the derived query; a null fragment means "" .
//   retireStale: cutoff = now minus `days` days; returns the repository's row count.
public class DeviceService {
    private final DeviceRepository repo;
    private final Clock clock;

    public DeviceService(DeviceRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    public DeviceView register(RegisterDeviceRequest r) { throw new UnsupportedOperationException("TODO"); }

    public DeviceView get(String serial) { throw new UnsupportedOperationException("TODO"); }

    public DeviceView patch(String serial, PatchDeviceRequest r) { throw new UnsupportedOperationException("TODO"); }

    public List<DeviceView> list(String modelFragment, DeviceStatus status) { throw new UnsupportedOperationException("TODO"); }

    public int retireStale(int days) { throw new UnsupportedOperationException("TODO"); }
}
