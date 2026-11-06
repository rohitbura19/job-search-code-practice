package practice.e;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The version check in patch() gives a fast, friendly 412 for the common case; the @Version column still protects the race where two
 * requests pass the check together (the second UPDATE matches 0 rows -> OptimisticLockingFailureException -> also 412).
 */
@Service
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository repo;

    public ProfileService(ProfileRepository repo) { this.repo = repo; }

    @Transactional
    public ProfileView create(CreateProfileRequest r) {
        String email = r.email().trim().toLowerCase();
        if (repo.existsByEmailIgnoreCase(email)) throw new Exceptions.DuplicateEmailException(email);
        try {
            return ProfileView.from(repo.saveAndFlush(new Profile(email, r.displayName(), new Address(r.city(), r.country().toUpperCase()))));
        } catch (DataIntegrityViolationException race) {
            throw new Exceptions.DuplicateEmailException(email);
        }
    }

    public ProfileView get(long id) {
        return repo.findById(id).map(ProfileView::from).orElseThrow(() -> new Exceptions.ProfileNotFoundException(id));
    }

    @Transactional
    public ProfileView patch(long id, long expectedVersion, PatchProfileRequest r) {
        Profile p = repo.findById(id).orElseThrow(() -> new Exceptions.ProfileNotFoundException(id));
        if (p.getVersion() != expectedVersion) throw new Exceptions.StaleVersionException(id, expectedVersion, p.getVersion());
        if (r.displayName() != null) p.setDisplayName(r.displayName());
        if (r.city() != null) p.setAddress(new Address(r.city(), p.getAddress().country()));
        return ProfileView.from(repo.saveAndFlush(p));
    }

    public Page<ProfileView> search(String city, String q, Pageable pageable) {
        return repo.findAll(ProfileSpecs.cityIs(city).and(ProfileSpecs.nameContains(q)), pageable).map(ProfileView::from);
    }
}
