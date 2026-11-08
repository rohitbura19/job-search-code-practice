package practice.e;

import jakarta.validation.constraints.*;

public record CreateStudentRequest(@NotBlank String name, @NotBlank @Email String email) {}
