package practice.e;

// TODO(E1): make this a JPA entity in table "profiles":
//  - Long id, database generated (IDENTITY)
//  - long version used for OPTIMISTIC LOCKING
//  - email: not null, unique column
//  - displayName: not null, max length 50
//  - address: the Address record stored INLINE as columns of this table (embedded)
//  JPA needs a protected no-arg constructor.
public class Profile {
    public Profile(String email, String displayName, Address address) { throw new UnsupportedOperationException("TODO"); }

    public Long getId() { throw new UnsupportedOperationException("TODO"); }
    public long getVersion() { throw new UnsupportedOperationException("TODO"); }
    public String getEmail() { throw new UnsupportedOperationException("TODO"); }
    public String getDisplayName() { throw new UnsupportedOperationException("TODO"); }
    public Address getAddress() { throw new UnsupportedOperationException("TODO"); }
    public void setDisplayName(String displayName) { throw new UnsupportedOperationException("TODO"); }
    public void setAddress(Address address) { throw new UnsupportedOperationException("TODO"); }
}
