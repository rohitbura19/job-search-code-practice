package practice.e;

/** PUT body. TODO: title not blank, max 100 chars; body max 2000 chars (may be null); version is required (optimistic-lock token). */
public record NoteUpdate(String title, String body, Long version) {}
