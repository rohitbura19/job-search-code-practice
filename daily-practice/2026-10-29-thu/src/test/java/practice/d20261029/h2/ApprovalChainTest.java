package practice.d20261029.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class ApprovalChainTest {
    static Expense ex(String id, long paise, String cat) { return new Expense(id, "asha", paise, cat); }

    static AbstractApprover chain() {
        CategoryBlocker head = new CategoryBlocker(Set.of("Gambling", "alcohol"));
        head.then(new LimitApprover("lead", 500_00)).then(new LimitApprover("manager", 5_000_00)).then(new LimitApprover("director", 50_000_00));
        return head;
    }

    @Test void firstCapableLinkDecides() {
        Approver c = chain();
        assertEquals("lead", c.handle(ex("e1", 100_00, "travel")).by());
        assertEquals("manager", c.handle(ex("e2", 2_000_00, "travel")).by());
        assertEquals("director", c.handle(ex("e3", 20_000_00, "hardware")).by());
        assertTrue(c.handle(ex("e3", 20_000_00, "hardware")).approved());
    }

    @Test void limitIsInclusive() {
        Approver c = chain();
        assertEquals("lead", c.handle(ex("a", 500_00, "x")).by());
        assertEquals("manager", c.handle(ex("b", 500_01, "x")).by());
    }

    @Test void blockedCategoryRejectsRegardlessOfAmountAndCase() {
        Approver c = chain();
        ApprovalResult r = c.handle(ex("e4", 1_00, "GAMBLING"));
        assertFalse(r.approved());
        assertEquals("policy", r.by());
        assertEquals("category blocked: GAMBLING", r.reason());
        assertFalse(c.handle(ex("e5", 1_00, "Alcohol")).approved());
    }

    @Test void endOfChainRejects() {
        ApprovalResult r = chain().handle(ex("e6", 50_000_01, "hardware"));
        assertFalse(r.approved());
        assertEquals("chain-end", r.by());
        assertEquals("no approver for 5000001", r.reason());
    }

    @Test void thenReturnsTheArgumentAndLinksReorder() {
        LimitApprover small = new LimitApprover("small", 10);
        LimitApprover big = new LimitApprover("big", 1000);
        assertSame(big, small.then(big));
        assertEquals("small", small.handle(ex("a", 10, "x")).by());
        assertEquals("big", small.handle(ex("b", 11, "x")).by());
        LimitApprover alone = new LimitApprover("alone", 10);
        assertEquals("chain-end", alone.handle(ex("c", 11, "x")).by());
    }

    @Test void handleCannotBeOverridden() throws Exception {
        assertTrue(java.lang.reflect.Modifier.isFinal(AbstractApprover.class.getMethod("handle", Expense.class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractApprover.class.getModifiers()));
    }

    @Test void loggingDecoratorRecordsAndDoesNotChangeTheResult() {
        List<String> log = new ArrayList<>();
        Approver logged = new LoggingApprover(chain(), log);
        ApprovalResult a = logged.handle(ex("e1", 100_00, "travel"));
        ApprovalResult b = logged.handle(ex("e2", 100_00, "gambling"));
        assertEquals(chain().handle(ex("e1", 100_00, "travel")), a);
        assertEquals(chain().handle(ex("e2", 100_00, "gambling")), b);
        assertEquals(List.of("e1:lead:approved", "e2:policy:rejected"), log);
    }

    @Test void expenseRejectsNonPositiveAmounts() {
        assertThrows(IllegalArgumentException.class, () -> ex("z", 0, "x"));
        assertThrows(IllegalArgumentException.class, () -> ex("z", -5, "x"));
    }
}
