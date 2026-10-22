package practice.d20261013.g;

/** GIVEN. Adapter: AWS IoT / MQTT hides behind this interface. Tests pass a fake that records the calls. */
public interface DeviceGateway {
    void notifyUpdate(String deviceId, String jobId, String firmwareUrl);
    void cancelUpdate(String deviceId, String jobId);
}
