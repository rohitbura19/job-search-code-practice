package practice.d20261011.g;

import java.util.List;

/** Chain of responsibility over providers of one channel (e.g. two SMS vendors). Tests: DispatchingTest. */
public class FailoverSender implements ChannelSender {

    public FailoverSender(Channel channel, List<ChannelSender> providers) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override public Channel channel() {
        throw new UnsupportedOperationException("TODO");
    }

    /** Providers in order. TransientFailure -> next provider. PermanentFailure -> rethrow at once. All failed -> rethrow the last TransientFailure. */
    @Override public SendResult send(Recipient r, RenderedMessage m) {
        throw new UnsupportedOperationException("TODO");
    }
}
