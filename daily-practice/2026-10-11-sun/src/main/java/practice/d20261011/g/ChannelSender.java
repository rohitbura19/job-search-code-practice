package practice.d20261011.g;

/** GIVEN - do not edit. Strategy: one implementation per channel (or per provider). Throws TransientFailure / PermanentFailure. */
public interface ChannelSender {
    Channel channel();
    SendResult send(Recipient r, RenderedMessage m);
}
