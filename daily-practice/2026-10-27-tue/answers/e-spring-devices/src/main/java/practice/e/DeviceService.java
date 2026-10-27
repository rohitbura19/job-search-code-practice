package practice.e;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class-level @Transactional, readOnly on the reads (no dirty-check flush, may route to a replica). patch() has no save():
 * the entity is managed inside the transaction, so Hibernate flushes the change at commit. The check-then-insert in register()
 * is only a friendly fast path; the unique constraint decides under concurrency.
 */
@Service
@Transactional
public class DeviceService {
    private final DeviceRepository repo;
    private final Clock clock;

    public DeviceService(DeviceRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    public DeviceView register(RegisterDeviceRequest r) {
        if (repo.existsBySerial(r.serial())) throw new DeviceConflictException(r.serial());
        Device d = new Device(r.serial(), r.model(), new Firmware(r.firmwareVersion(), r.channel()), DeviceStatus.ACTIVE, clock.instant());
        return DeviceView.from(repo.save(d));
    }

    @Transactional(readOnly = true)
    public DeviceView get(String serial) {
        return repo.findBySerial(serial).map(DeviceView::from).orElseThrow(() -> new DeviceNotFoundException(serial));
    }

    public DeviceView patch(String serial, PatchDeviceRequest r) {
        Device d = repo.findBySerial(serial).orElseThrow(() -> new DeviceNotFoundException(serial));
        if (r.model() != null) d.setModel(r.model());
        if (r.channel() != null) d.setFirmware(new Firmware(d.getFirmware().version(), r.channel()));
        return DeviceView.from(d);
    }

    @Transactional(readOnly = true)
    public List<DeviceView> list(String modelFragment, DeviceStatus status) {
        String fragment = modelFragment == null ? "" : modelFragment;
        return repo.findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc(fragment, status).stream().map(DeviceView::from).toList();
    }

    public int retireStale(int days) {
        return repo.retireSeenBefore(clock.instant().minus(Duration.ofDays(days)));
    }
}
