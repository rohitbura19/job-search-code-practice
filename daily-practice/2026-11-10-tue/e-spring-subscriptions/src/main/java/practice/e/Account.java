package practice.e;

import java.time.Instant;

/**
 * TODO(E1a): make this an entity mapped to table "accounts".
 *  - id: IDENTITY generated. email: not null, length 120, UNIQUE (a duplicate insert must fail at the database). createdAt: plain Instant column.
 */
public class Account {
    private Long id;
    private String email;
    private Instant createdAt;

    protected Account() {}
    public Account(String email, Instant createdAt) { this.email = email; this.createdAt = createdAt; }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}
