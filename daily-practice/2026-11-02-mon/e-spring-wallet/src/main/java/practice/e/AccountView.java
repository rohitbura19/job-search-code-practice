package practice.e;

import java.math.BigDecimal;

/** Given. */
public record AccountView(Long id, String owner, BigDecimal balance) {
    static AccountView of(Account a) { return new AccountView(a.getId(), a.getOwner(), a.getBalance()); }
}
