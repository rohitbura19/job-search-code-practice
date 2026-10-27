package practice.e;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDeviceRequest(
        @NotNull @ValidSerial String serial,
        @NotBlank @Size(max = 40) String model,
        @NotNull @Pattern(regexp = "\\d+\\.\\d+\\.\\d+", message = "must be major.minor.patch") String firmwareVersion,
        @NotNull Channel channel) {}
