package practice.e;

import jakarta.persistence.*;

/** @Version gives optimistic locking: Hibernate adds "where id=? and version=?" to every UPDATE and bumps the version; 0 rows updated -> exception. */
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String displayName;

    @Embedded
    private Address address;

    protected Profile() {}

    public Profile(String email, String displayName, Address address) {
        this.email = email;
        this.displayName = displayName;
        this.address = address;
    }

    public Long getId() { return id; }
    public long getVersion() { return version; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public Address getAddress() { return address; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setAddress(Address address) { this.address = address; }
}
