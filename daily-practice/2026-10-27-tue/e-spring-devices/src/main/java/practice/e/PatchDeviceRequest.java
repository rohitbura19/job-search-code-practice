package practice.e;

// TODO: a PARTIAL update: both fields are optional (null = leave unchanged), but model, when present, must be 1..40 characters.
public record PatchDeviceRequest(String model, Channel channel) {}
