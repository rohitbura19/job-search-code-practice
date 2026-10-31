package practice.e;

/**
 * POST body. TODO add bean-validation annotations:
 *   owner: required, matches [a-z0-9_]{3,20};  title: not blank, max 100 chars;  body: optional, max 2000 chars.
 */
public record NoteRequest(String owner, String title, String body) {}
