package practice.e;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String serial) { super("No device with serial " + serial); }
}
