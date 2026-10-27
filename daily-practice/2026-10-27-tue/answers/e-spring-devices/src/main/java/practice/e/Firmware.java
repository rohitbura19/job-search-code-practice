package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/** Embeddable value object: its columns live in the owner's table. Explicit column names avoid clashes and keywords. */
@Embeddable
public record Firmware(
        @Column(name = "fw_version", nullable = false) String version,
        @Enumerated(EnumType.STRING) @Column(name = "fw_channel", nullable = false, length = 16) Channel channel) {}
