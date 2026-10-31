package practice.e;

import java.time.Instant;

public record NoteView(long id, String owner, String title, String body, long version, Instant createdAt) {
    static NoteView of(Note n) { return new NoteView(n.getId(), n.getOwner(), n.getTitle(), n.getBody(), n.getVersion(), n.getCreatedAt()); }
}
