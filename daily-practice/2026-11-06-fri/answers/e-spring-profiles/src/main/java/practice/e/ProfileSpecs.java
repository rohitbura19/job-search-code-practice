package practice.e;

import org.springframework.data.jpa.domain.Specification;

/** Each spec returns cb.conjunction() (always true) for a null argument, so callers can and() them without if-chains. */
public final class ProfileSpecs {
    private ProfileSpecs() {}

    public static Specification<Profile> cityIs(String city) {
        return (root, query, cb) -> city == null ? cb.conjunction() : cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase());
    }

    public static Specification<Profile> nameContains(String part) {
        return (root, query, cb) -> part == null ? cb.conjunction() : cb.like(cb.lower(root.get("displayName")), "%" + part.toLowerCase() + "%");
    }
}
