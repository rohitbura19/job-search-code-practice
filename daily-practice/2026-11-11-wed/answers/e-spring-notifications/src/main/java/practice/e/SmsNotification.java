package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@DiscriminatorValue("SMS")
public class SmsNotification extends Notification {
    @Column(length = 20)
    private String phone;

    protected SmsNotification() {}
    public SmsNotification(Long userId, String phone, String message, Instant createdAt) { super(userId, message, createdAt); this.phone = phone; }

    public String getPhone() { return phone; }
    @Override public Channel channel() { return Channel.SMS; }
    @Override public String target() { return phone; }
}
