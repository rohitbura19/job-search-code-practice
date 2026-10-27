package practice.e;

import java.time.Instant;

public record DeviceView(String serial, String model, String firmwareVersion, Channel channel, DeviceStatus status, Instant lastSeen) {
    public static DeviceView from(Device d) {
        return new DeviceView(d.getSerial(), d.getModel(), d.getFirmware().version(), d.getFirmware().channel(), d.getStatus(), d.getLastSeen());
    }
}
