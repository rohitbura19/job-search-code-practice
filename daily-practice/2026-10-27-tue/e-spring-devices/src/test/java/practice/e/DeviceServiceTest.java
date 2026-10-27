package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {
    static final Instant NOW = Instant.parse("2026-10-27T04:00:00Z");
    @Mock DeviceRepository repo;
    DeviceService service;

    @BeforeEach void setUp() { service = new DeviceService(repo, Clock.fixed(NOW, ZoneOffset.UTC)); }

    static Device device() { return new Device("AB-123455", "Charger", new Firmware("1.2.3", Channel.STABLE), DeviceStatus.ACTIVE, NOW); }

    @Test void registerSavesAnActiveDeviceSeenNow() {
        when(repo.existsBySerial("AB-123455")).thenReturn(false);
        when(repo.save(any(Device.class))).thenAnswer(i -> i.getArgument(0));
        DeviceView v = service.register(new RegisterDeviceRequest("AB-123455", "Charger", "2.0.1", Channel.BETA));
        ArgumentCaptor<Device> c = ArgumentCaptor.forClass(Device.class);
        verify(repo).save(c.capture());
        assertEquals(DeviceStatus.ACTIVE, c.getValue().getStatus());
        assertEquals(NOW, c.getValue().getLastSeen());
        assertEquals("2.0.1", v.firmwareVersion());
        assertEquals(Channel.BETA, v.channel());
    }

    @Test void registerRejectsAnExistingSerialWithoutSaving() {
        when(repo.existsBySerial("AB-123455")).thenReturn(true);
        assertThrows(DeviceConflictException.class, () -> service.register(new RegisterDeviceRequest("AB-123455", "x", "1.0.0", Channel.STABLE)));
        verify(repo, never()).save(any());
    }

    @Test void getMapsOrThrows() {
        when(repo.findBySerial("AB-123455")).thenReturn(Optional.of(device()));
        when(repo.findBySerial("ZZ-000000")).thenReturn(Optional.empty());
        assertEquals("Charger", service.get("AB-123455").model());
        assertThrows(DeviceNotFoundException.class, () -> service.get("ZZ-000000"));
    }

    @Test void patchChangesOnlyProvidedFieldsRelyingOnDirtyChecking() {
        Device d = device();
        when(repo.findBySerial("AB-123455")).thenReturn(Optional.of(d));
        DeviceView v = service.patch("AB-123455", new PatchDeviceRequest(null, Channel.BETA));
        assertEquals("Charger", d.getModel());
        assertEquals(new Firmware("1.2.3", Channel.BETA), d.getFirmware());
        assertEquals(Channel.BETA, v.channel());
        service.patch("AB-123455", new PatchDeviceRequest("Renamed", null));
        assertEquals("Renamed", d.getModel());
        assertEquals(Channel.BETA, d.getFirmware().channel());
        service.patch("AB-123455", new PatchDeviceRequest(null, null));
        verify(repo, never()).save(any());
    }

    @Test void patchOfUnknownSerialIsNotFound() {
        when(repo.findBySerial("ZZ-000000")).thenReturn(Optional.empty());
        assertThrows(DeviceNotFoundException.class, () -> service.patch("ZZ-000000", new PatchDeviceRequest("x", null)));
    }

    @Test void listTreatsNullFragmentAsEmpty() {
        when(repo.findByModelContainingIgnoreCaseAndStatusOrderBySerialAsc("", DeviceStatus.ACTIVE)).thenReturn(List.of(device()));
        assertEquals(1, service.list(null, DeviceStatus.ACTIVE).size());
    }

    @Test void retireStaleUsesTheClockMinusDays() {
        when(repo.retireSeenBefore(NOW.minusSeconds(86_400L * 30))).thenReturn(7);
        assertEquals(7, service.retireStale(30));
    }

    @Test void transactionBoundariesAreDeclared() throws Exception {
        assertTrue(DeviceService.class.isAnnotationPresent(Transactional.class));
        assertTrue(DeviceService.class.getMethod("get", String.class).getAnnotation(Transactional.class).readOnly());
        assertTrue(DeviceService.class.getMethod("list", String.class, DeviceStatus.class).getAnnotation(Transactional.class).readOnly());
        assertFalse(DeviceService.class.getAnnotation(Transactional.class).readOnly());
    }
}
