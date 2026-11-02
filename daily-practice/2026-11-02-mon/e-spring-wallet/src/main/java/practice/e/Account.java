package practice.e;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Given. @Version enables optimistic locking; the pessimistic lock in the repository is the first line of defence for transfers. */
@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(name = "uk_account_owner", columnNames = "owner"))
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 40) private String owner;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal balance;
    @Version private long version;

    protected Account() {}
    public Account(String owner, BigDecimal balance) { this.owner = owner; this.balance = balance; }

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public long getVersion() { return version; }
}
