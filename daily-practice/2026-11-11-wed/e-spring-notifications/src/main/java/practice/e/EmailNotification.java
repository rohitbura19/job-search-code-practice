package practice.e;

import java.time.Instant;

/** TODO(E1b): @Entity with @DiscriminatorValue("EMAIL"); `address` is a column of length 200 (it must stay nullable - why? see the README). */
public class EmailNotification extends Notification {
    private String address;

    protected EmailNotification() {}
    public EmailNotification(Long userId, String address, String message, Instant createdAt) { super(userId, message, createdAt); this.address = address; }

    public String getAddress() { return address; }
    @Override public Channel channel() { return Channel.EMAIL; }
    @Override public String target() { return address; }
}
