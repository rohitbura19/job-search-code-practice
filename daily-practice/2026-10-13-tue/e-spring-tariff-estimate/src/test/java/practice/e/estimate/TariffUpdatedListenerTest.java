package practice.e.estimate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** E - the SQS message handler as a plain method (the @SqsListener wiring is paper). */
class TariffUpdatedListenerTest {

    final TariffLookup lookup = mock(TariffLookup.class);
    final TariffUpdatedListener listener = new TariffUpdatedListener(lookup);

    @Test @DisplayName("a message evicts exactly that region")
    void evicts() {
        listener.on(new TariffUpdated("IN-KA"));
        verify(lookup).evict("IN-KA");
        verifyNoMoreInteractions(lookup);
    }

    @Test @DisplayName("failures propagate unchanged, so SQS does not delete the message and redelivers it")
    void failuresPropagate() {
        IllegalStateException boom = new IllegalStateException("cache down");
        doThrow(boom).when(lookup).evict("IN-KA");
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> listener.on(new TariffUpdated("IN-KA")));
        assertSame(boom, thrown);
    }
}
