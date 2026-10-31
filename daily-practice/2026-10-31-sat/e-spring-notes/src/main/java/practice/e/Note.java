package practice.e;

/**
 * TODO: JPA entity, table "notes".
 *   id (generated, IDENTITY), owner, title (not null, max 100), body (max 2000, nullable), createdAt (Instant set on insert by Hibernate),
 *   version (optimistic lock), deleted (boolean, default false).
 *   Unique: (owner, title).
 *   Soft delete: repository.delete(note) must run "update notes set deleted = true where id = ? and version = ?" instead of a DELETE,
 *   and every query/find must ignore rows where deleted = true (Hibernate annotations, not hand-written filters).
 * Needs a no-arg constructor for JPA; the (owner, title, body) constructor and the getters are given.
 */
public class Note {
    protected Note() {}

    public Note(String owner, String title, String body) { throw new UnsupportedOperationException("TODO"); }

    public Long getId() { throw new UnsupportedOperationException("TODO"); }
    public String getOwner() { throw new UnsupportedOperationException("TODO"); }
    public String getTitle() { throw new UnsupportedOperationException("TODO"); }
    public String getBody() { throw new UnsupportedOperationException("TODO"); }
    public java.time.Instant getCreatedAt() { throw new UnsupportedOperationException("TODO"); }
    public long getVersion() { throw new UnsupportedOperationException("TODO"); }
    public void edit(String title, String body) { throw new UnsupportedOperationException("TODO"); }
}
