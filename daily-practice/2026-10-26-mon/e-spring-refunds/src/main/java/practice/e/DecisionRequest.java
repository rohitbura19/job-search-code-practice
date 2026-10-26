package practice.e;

import jakarta.validation.constraints.NotNull;

public record DecisionRequest(@NotNull Boolean approved) {}
