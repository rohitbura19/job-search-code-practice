package practice.d20261011.g;

import java.util.List;

/** Registry / factory: channel -> sender. Immutable after construction. Tests: DispatchingTest. */
public class SenderRegistry {

    /** Two senders for one channel: throw IllegalStateException. */
    public SenderRegistry(List<ChannelSender> senders) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Throws IllegalArgumentException for a channel with no sender. */
    public ChannelSender get(Channel c) {
        throw new UnsupportedOperationException("TODO");
    }
}
