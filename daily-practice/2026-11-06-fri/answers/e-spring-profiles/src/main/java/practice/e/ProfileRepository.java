package practice.e;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** JpaSpecificationExecutor adds findAll(Specification, Pageable): filters are composed at runtime, null filters simply drop out. */
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
