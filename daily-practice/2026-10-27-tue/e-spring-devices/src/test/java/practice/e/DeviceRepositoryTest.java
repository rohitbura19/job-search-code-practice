package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class DeviceRepositoryTest {
    static final Instant NOW = Instant.parse("2026-10-27T04:00:00Z");
    @Autowired DeviceRepository repo;
    @Autowired JdbcTemplate jdbc;

    static Device dev(String serial, String model, Channel ch, DeviceStatus st, Instant seen) {
        return new Device(serial, model, new Firmware("1.2.3", ch), st, seen);
    }

    @BeforeEach void seed() {
        repo.save(dev("AB-123455", "Charger Pro", Channel.STABLE, DeviceStatus.ACTIVE, NOW.minusSeconds(86_400L * 100)));
        repo.save(dev("CD-111115", "charger mini", Channel.BETA, DeviceStatus.ACTIVE, NOW.minusSeconds(86_400L * 10)));
        repo.save(dev("EF-222220", "Gateway", Channel.BETA, DeviceStatus.ACTIVE, null));
        repo.saveAndFlush(dev("GH-555555", "Charger Old", Channel.STABLE, DeviceStatus.RETIRED, NOW.minusSeconds(86_400L * 400)));
    }

    @Test void findAndExistsBySerial() {
        assertTrue(repo.findBySerial("AB-123455").isPresent());
        assertTrue(repo.findBySerial("ZZ-000000").isEmpty());
        assertTrue(repo.existsBySerial("EF-222220"));
        assertFalse(repo.existsBySerial("ZZ-000000"));
    }

    @Test void duplicateSerialViolatesTheUniqueConstraint() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(dev("AB-123455", "Dup", Channel.STABLE, DeviceStatus.ACTIVE, NOW)));
    }

    @Test void modelSearchIsCaseInsensitiveFiltersStatusAndIsSortedBySerial() {
        List<Device> active = repo.findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc("CHARGER", DeviceStatus.ACTIVE);
        assertEquals(List.of("AB-123455", "CD-111115"), active.stream().map(Device::getSerial).toList());
        assertEquals(3, repo.findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc("", DeviceStatus.ACTIVE).size());
        assertEquals(List.of("GH-555555"), repo.findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc("old", DeviceStatus.RETIRED).stream().map(Device::getSerial).toList());
    }

    @Test void nestedEmbeddedPropertyQuery() {
        assertEquals(List.of("CD-111115", "EF-222220"), repo.findByFirmwareChannelOrderBySerialAsc(Channel.BETA).stream().map(Device::getSerial).toList());
    }

    @Test void mappingUsesTheExplicitColumnsAndStoresEnumsAsText() {
        assertEquals("1.2.3", jdbc.queryForObject("select fw_version from device where serial = 'AB-123455'", String.class));
        assertEquals("BETA", jdbc.queryForObject("select fw_channel from device where serial = 'CD-111115'", String.class));
        assertEquals("RETIRED", jdbc.queryForObject("select status from device where serial = 'GH-555555'", String.class));
    }

    @Test void bulkRetireCountsOnlyActiveDevicesSeenBeforeTheCutoffAndSkipsNeverSeen() {
        Device before = repo.findBySerial("AB-123455").orElseThrow();       // loaded into the persistence context
        assertEquals(DeviceStatus.ACTIVE, before.getStatus());
        int n = repo.retireSeenBefore(NOW.minusSeconds(86_400L * 90));
        assertEquals(1, n);
        // no manual em.clear(): the @Modifying attribute must have refreshed the context
        assertEquals(DeviceStatus.RETIRED, repo.findBySerial("AB-123455").orElseThrow().getStatus());
        assertEquals(DeviceStatus.ACTIVE, repo.findBySerial("CD-111115").orElseThrow().getStatus());
        assertEquals(DeviceStatus.ACTIVE, repo.findBySerial("EF-222220").orElseThrow().getStatus());
        assertEquals(0, repo.retireSeenBefore(NOW.minusSeconds(86_400L * 90)));
    }
}
