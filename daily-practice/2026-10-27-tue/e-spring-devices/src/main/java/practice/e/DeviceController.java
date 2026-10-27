package practice.e;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

// TODO: REST controller under /api/devices; no logic here, only delegation. Do NOT put @Validated on the class.
//   POST  /api/devices                          valid body -> 201 + Location /api/devices/{serial} + the view
//   GET   /api/devices/{serial}                 -> 200 + view
//   PATCH /api/devices/{serial}                 valid body -> 200 + view
//   GET   /api/devices?model=&status=           model defaults to "" and status to ACTIVE -> 200 + list (an unknown status must be a 400)
//   POST  /api/devices/maintenance/retire-stale?days=   days defaults to 90 and must be positive -> 200 {"retired": n}
public class DeviceController {
    private final DeviceService service;

    public DeviceController(DeviceService service) { this.service = service; }

    public ResponseEntity<DeviceView> register(RegisterDeviceRequest request) { throw new UnsupportedOperationException("TODO"); }

    public DeviceView get(String serial) { throw new UnsupportedOperationException("TODO"); }

    public DeviceView patch(String serial, PatchDeviceRequest request) { throw new UnsupportedOperationException("TODO"); }

    public List<DeviceView> list(String model, DeviceStatus status) { throw new UnsupportedOperationException("TODO"); }

    public Map<String, Integer> retireStale(int days) { throw new UnsupportedOperationException("TODO"); }
}
