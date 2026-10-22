package practice.e;

import java.util.UUID;

/** GIVEN - the Kafka value. */
public record CommandMessage(UUID id, String chargerId, CommandType type) {
    public static CommandMessage from(CommandCreated e) { return new CommandMessage(e.id(), e.chargerId(), e.type()); }
}
