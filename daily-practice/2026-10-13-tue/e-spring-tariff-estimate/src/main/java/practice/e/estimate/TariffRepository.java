package practice.e.estimate;

import java.util.Optional;

/** GIVEN - stands in for the Spring Data JpaRepository (derived query findByRegionCode). Tests mock it. */
public interface TariffRepository {
    Optional<Tariff> findByRegionCode(String regionCode);
}
