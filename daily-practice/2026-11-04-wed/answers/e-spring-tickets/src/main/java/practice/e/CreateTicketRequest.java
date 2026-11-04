package practice.e;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** @Email and @Size accept null, so an absent assignee is valid. @NotBlank covers null, "" and whitespace. */
public record CreateTicketRequest(
        @NotBlank @Size(max = 100) @NotAllCaps String title,
        @NotNull Priority priority,
        @Email @Size(max = 80) String assignee) {}
