package practice.d20261102.h2;

import static org.junit.jupiter.api.Assertions.*;

import practice.d20261102.h2.VendingMachine.Dispensed;
import practice.d20261102.h2.VendingMachine.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VendingMachineTest {
    VendingMachine vm;

    @BeforeEach void setUp() {
        vm = new VendingMachine();
        vm.stock("A1", "chips", 150, 2);
        vm.stock("B2", "gum", 75, 0);
    }

    @Test void startsIdleWithNoCredit() {
        assertEquals(Status.IDLE, vm.status());
        assertEquals(0, vm.credit());
        assertEquals(0, vm.refund());
        assertEquals(Status.IDLE, vm.status());
    }

    @Test void insertAccumulatesCredit() {
        vm.insert(100);
        assertEquals(Status.HAS_CREDIT, vm.status());
        vm.insert(25);
        assertEquals(125, vm.credit());
    }

    @Test void purchaseReturnsChangeAndResetsToIdle() {
        vm.insert(100); vm.insert(100);
        assertEquals(new Dispensed("chips", 50), vm.select("A1"));
        assertEquals(Status.IDLE, vm.status());
        assertEquals(0, vm.credit());
    }

    @Test void exactPaymentGivesZeroChange() {
        vm.insert(150);
        assertEquals(0, vm.select("A1").changeCents());
    }

    @Test void selectWithoutCreditFails() {
        assertThrows(IllegalStateException.class, () -> vm.select("A1"));
        assertThrows(IllegalArgumentException.class, () -> vm.select("ZZ"));
    }

    @Test void insufficientCreditKeepsCredit() {
        vm.insert(100);
        assertThrows(IllegalStateException.class, () -> vm.select("A1"));
        assertEquals(100, vm.credit());
        assertEquals(Status.HAS_CREDIT, vm.status());
        vm.insert(50);
        assertEquals("chips", vm.select("A1").item());
    }

    @Test void soldOutKeepsCreditAndUnknownSlotRejected() {
        vm.insert(500);
        assertThrows(IllegalStateException.class, () -> vm.select("B2"));
        assertThrows(IllegalArgumentException.class, () -> vm.select("ZZ"));
        assertEquals(500, vm.credit());
    }

    @Test void stockRunsOut() {
        vm.insert(450);
        vm.select("A1");
        vm.insert(300);
        vm.select("A1");
        vm.insert(300);
        assertThrows(IllegalStateException.class, () -> vm.select("A1"));
    }

    @Test void refundReturnsCreditOnce() {
        vm.insert(60); vm.insert(40);
        assertEquals(100, vm.refund());
        assertEquals(Status.IDLE, vm.status());
        assertEquals(0, vm.refund());
    }

    @Test void outOfServiceBlocksInsertAndSelectButAllowsRefund() {
        vm.insert(200);
        vm.setOutOfService(true);
        assertEquals(Status.OUT_OF_SERVICE, vm.status());
        assertEquals(200, vm.credit());
        assertThrows(IllegalStateException.class, () -> vm.insert(10));
        assertThrows(IllegalStateException.class, () -> vm.select("A1"));
        assertEquals(200, vm.refund());
        assertEquals(Status.OUT_OF_SERVICE, vm.status());
        vm.setOutOfService(false);
        assertEquals(Status.IDLE, vm.status());
    }

    @Test void backInServiceWithCreditResumes() {
        vm.insert(150);
        vm.setOutOfService(true);
        vm.setOutOfService(false);
        assertEquals(Status.HAS_CREDIT, vm.status());
        assertEquals("chips", vm.select("A1").item());
    }

    @Test void validation() {
        assertThrows(IllegalArgumentException.class, () -> vm.insert(0));
        assertThrows(IllegalArgumentException.class, () -> vm.insert(-5));
        assertThrows(IllegalArgumentException.class, () -> vm.stock("C", "x", 0, 1));
        assertThrows(IllegalArgumentException.class, () -> vm.stock("C", "x", 10, -1));
        vm.stock("A1", "chips", 200, 1);    // replaces the slot
        vm.insert(150);
        assertThrows(IllegalStateException.class, () -> vm.select("A1"));
    }
}
