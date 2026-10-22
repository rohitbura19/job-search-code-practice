package practice.e;

import java.util.UUID;

/** GIVEN - in-process event published by the service inside the transaction. */
public record CommandCreated(UUID id, String chargerId, CommandType type) {}
