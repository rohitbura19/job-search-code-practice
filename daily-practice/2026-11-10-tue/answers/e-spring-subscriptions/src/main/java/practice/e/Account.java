package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120, unique = true)
    private String email;
    private Instant createdAt;

    protected Account() {}
    public Account(String email, Instant createdAt) { this.email = email; this.createdAt = createdAt; }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}
