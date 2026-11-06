package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// TODO(E5): make this a Spring bean, read-only transactional by default, with writes transactional.
public class ProfileService {
    public ProfileService(ProfileRepository repo) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Email is trimmed and lower-cased before it is checked and stored; country is stored upper-case.
     * DuplicateEmailException if the email exists - also when the unique constraint fires in a race (DataIntegrityViolationException).
     * Flush so the constraint fires here.
     */
    public ProfileView create(CreateProfileRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** ProfileNotFoundException when absent. */
    public ProfileView get(long id) { throw new UnsupportedOperationException("TODO"); }

    /**
     * ProfileNotFoundException when absent; StaleVersionException(id, expectedVersion, actualVersion) when the stored version differs.
     * Non-null fields overwrite (a new city keeps the country). Returns the view AFTER flush, so the version is the new one.
     */
    public ProfileView patch(long id, long expectedVersion, PatchProfileRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** Both filters optional (null = no filter), combined with AND, via ProfileSpecs. */
    public Page<ProfileView> search(String city, String q, Pageable pageable) { throw new UnsupportedOperationException("TODO"); }
}
