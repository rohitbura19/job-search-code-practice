package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@DataJpaTest
@Import({ShipmentService.class, ShipmentServiceTest.Config.class})
class ShipmentServiceTest {
    static final Instant NOW = Instant.parse("2026-11-05T10:00:00Z");

    @TestConfiguration
    static class Config { @Bean @Primary Clock testClock() { return Clock.fixed(NOW, ZoneOffset.UTC); } }

    @Autowired ShipmentService service;
    @Autowired ShipmentRepository repo;
    @Autowired ProcessedEventRepository processed;
    @MockBean ShipmentPublisher publisher;

    @Test void createStripsSavesAndPublishesOnce() {
        ShipmentView v = service.create(new CreateShipmentRequest("  o-1 ", " 12 Main St "));
        assertThat(v.orderId()).isEqualTo("o-1");
        assertThat(v.address()).isEqualTo("12 Main St");
        assertThat(v.status()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(v.trackingId()).isEqualTo("TRK-o-1");
        assertThat(v.updatedAt()).isEqualTo(NOW);
        ArgumentCaptor<Shipment> c = ArgumentCaptor.forClass(Shipment.class);
        verify(publisher, times(1)).publish(c.capture());
        assertThat(c.getValue().getOrderId()).isEqualTo("o-1");
    }

    @Test void duplicateOrderRejectedWithoutPublishing() {
        service.create(new CreateShipmentRequest("dup", "a"));
        assertThatThrownBy(() -> service.create(new CreateShipmentRequest(" dup ", "b"))).isInstanceOf(Exceptions.DuplicateOrderException.class);
        verify(publisher, times(1)).publish(any());
        assertThat(repo.count()).isEqualTo(1);
    }

    @Test void getUnknownIsNotFound() {
        assertThatThrownBy(() -> service.get(999L)).isInstanceOf(Exceptions.ShipmentNotFoundException.class);
    }

    @Test void carrierUpdateAdvancesAndPublishes() {
        service.create(new CreateShipmentRequest("o1", "a"));
        boolean moved = service.applyCarrierUpdate(new CarrierUpdate("e1", "TRK-o1", ShipmentStatus.IN_TRANSIT));
        assertThat(moved).isTrue();
        assertThat(repo.findByOrderId("o1").orElseThrow().getStatus()).isEqualTo(ShipmentStatus.IN_TRANSIT);
        verify(publisher, times(2)).publish(any());
    }

    @Test void redeliveredEventIsIgnored() {
        service.create(new CreateShipmentRequest("o1", "a"));
        CarrierUpdate u = new CarrierUpdate("e1", "TRK-o1", ShipmentStatus.PICKED_UP);
        assertThat(service.applyCarrierUpdate(u)).isTrue();
        assertThat(service.applyCarrierUpdate(u)).isFalse();
        verify(publisher, times(2)).publish(any());
        assertThat(processed.existsById("e1")).isTrue();
    }

    @Test void outOfOrderUpdateIsRecordedButDoesNotMoveBackwards() {
        service.create(new CreateShipmentRequest("o1", "a"));
        service.applyCarrierUpdate(new CarrierUpdate("e2", "TRK-o1", ShipmentStatus.OUT_FOR_DELIVERY));
        assertThat(service.applyCarrierUpdate(new CarrierUpdate("e1", "TRK-o1", ShipmentStatus.PICKED_UP))).isFalse();
        assertThat(repo.findByOrderId("o1").orElseThrow().getStatus()).isEqualTo(ShipmentStatus.OUT_FOR_DELIVERY);
        assertThat(processed.existsById("e1")).isTrue();
    }

    @Test void unknownTrackingIdIsIgnored() {
        assertThat(service.applyCarrierUpdate(new CarrierUpdate("e9", "TRK-zzz", ShipmentStatus.DELIVERED))).isFalse();
        verify(publisher, never()).publish(any());
    }

    @Test void searchClampsPageAndSize() {
        for (int i = 0; i < 3; i++) service.create(new CreateShipmentRequest("o" + i, "a"));
        ShipmentPage p = service.search(null, -5, 0);
        assertThat(p.page()).isZero();
        assertThat(p.size()).isEqualTo(1);
        assertThat(p.content()).hasSize(1);
        assertThat(p.total()).isEqualTo(3);
        assertThat(service.search(null, 0, 10_000).size()).isEqualTo(50);
        assertThat(service.search(ShipmentStatus.DELIVERED, 0, 10).content()).isEmpty();
    }
}
