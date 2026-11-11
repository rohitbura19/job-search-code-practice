package practice.e;

import java.time.Instant;

/** TODO(E1c): @Entity with @DiscriminatorValue("SMS"); `phone` is a column of length 20. */
public class SmsNotification extends Notification {
    private String phone;

    protected SmsNotification() {}
    public SmsNotification(Long userId, String phone, String message, Instant createdAt) { super(userId, message, createdAt); this.phone = phone; }

    public String getPhone() { return phone; }
    @Override public Channel channel() { return Channel.SMS; }
    @Override public String target() { return phone; }
}
