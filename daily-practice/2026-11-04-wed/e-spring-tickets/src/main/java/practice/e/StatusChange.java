package practice.e;

import jakarta.validation.constraints.NotNull;

/** Given. Body of PATCH /api/tickets/{id}/status. */
public record StatusChange(@NotNull Status status) {}
