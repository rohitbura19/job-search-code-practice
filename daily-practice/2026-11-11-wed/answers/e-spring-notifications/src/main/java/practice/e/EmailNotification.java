package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@DiscriminatorValue("EMAIL")
public class EmailNotification extends Notification {
    @Column(length = 200)               // nullable: in SINGLE_TABLE the column is empty for SMS rows
    private String address;

    protected EmailNotification() {}
    public EmailNotification(Long userId, String address, String message, Instant createdAt) { super(userId, message, createdAt); this.address = address; }

    public String getAddress() { return address; }
    @Override public Channel channel() { return Channel.EMAIL; }
    @Override public String target() { return address; }
}
