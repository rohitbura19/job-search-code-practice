package practice.e;

import jakarta.validation.constraints.Size;

/** Null means "not provided". @Size ignores null, so an absent model passes while "" and 41 characters fail. */
public record PatchDeviceRequest(@Size(min = 1, max = 40) String model, Channel channel) {}
