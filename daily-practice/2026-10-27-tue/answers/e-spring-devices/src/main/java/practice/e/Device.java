package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

/** The named unique constraint is the real guard against double registration; ORDINAL would silently break when the enum is reordered. */
@Entity
@Table(name = "device", uniqueConstraints = @UniqueConstraint(name = "uk_device_serial", columnNames = "serial"))
public class Device {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 9)
    private String serial;

    @Column(nullable = false, length = 40)
    private String model;

    @Embedded
    private Firmware firmware;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
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
