package practice.e;

// TODO: Bean Validation (messages optional):
//   serial: required + your @ValidSerial; model: not blank, at most 40 chars;
//   firmwareVersion: required, "major.minor.patch" digits only; channel: required.
public record RegisterDeviceRequest(String serial, String model, String firmwareVersion, Channel channel) {}
