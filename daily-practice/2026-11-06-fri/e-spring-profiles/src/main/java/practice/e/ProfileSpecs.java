package practice.e;

import org.springframework.data.jpa.domain.Specification;

public final class ProfileSpecs {
    private ProfileSpecs() {}

    /** TODO(E3): profiles whose address.city equals the argument ignoring case. A null argument matches everything (cb.conjunction()). */
    public static Specification<Profile> cityIs(String city) { throw new UnsupportedOperationException("TODO"); }

    /** TODO(E3): displayName contains the argument, ignoring case (LIKE). A null argument matches everything. */
    public static Specification<Profile> nameContains(String part) { throw new UnsupportedOperationException("TODO"); }
}
