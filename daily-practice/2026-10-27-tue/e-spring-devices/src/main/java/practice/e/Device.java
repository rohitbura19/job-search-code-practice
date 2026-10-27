package practice.e;

import java.time.Instant;

// TODO: make this a JPA entity.
//   - table "device" with a NAMED unique constraint uk_device_serial on serial (the guard against the double-registration race)
//   - id: database generated (IDENTITY); serial NOT NULL length 9; model NOT NULL length 40
//   - firmware: embedded (see Firmware); status: NOT NULL, stored as a String (EnumType.STRING) - why never ORDINAL?
//   - lastSeen: nullable
//   - JPA needs a no-arg constructor: keep it protected
public class Device {
    private Long id;
    private String serial;
    private String model;
    private Firmware firmware;
    private DeviceStatus status;
    private Instant lastSeen;

    protected Device() {}

    public Device(String serial, String model, Firmware firmware, DeviceStatus status, Instant lastSeen) {
        this.serial = serial;
        this.model = model;
        this.firmware = firmware;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    public Long getId() { return id; }
    public String getSerial() { return serial; }
    public String getModel() { return model; }
    public Firmware getFirmware() { return firmware; }
    public DeviceStatus getStatus() { return status; }
    public Instant getLastSeen() { return lastSeen; }
    public void setModel(String model) { this.model = model; }
    public void setFirmware(Firmware firmware) { this.firmware = firmware; }
}
