package practice.e;

// TODO: Bean Validation on the record components:
//  title:    required text (not blank), at most 100 characters, and @NotAllCaps
//  priority: required
//  assignee: optional; when present it must be a well-formed e-mail address of at most 80 characters
public record CreateTicketRequest(String title, Priority priority, String assignee) {}
