package practice.e;

public class DeviceConflictException extends RuntimeException {
    public DeviceConflictException(String serial) { super("Serial already registered: " + serial); }
}
