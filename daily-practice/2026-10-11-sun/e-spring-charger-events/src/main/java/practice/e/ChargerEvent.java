package practice.e;

import java.time.Instant;

/**
 * GIVEN - do not edit. In the real application this is the JPA entity from the question (@Entity, @Id @GeneratedValue(IDENTITY),
 * @Enumerated(STRING), unique eventId). It is a plain class here so no database is needed; write the annotations on paper.
 */
public class ChargerEvent {
    public enum Status { AVAILABLE, CHARGING, FAULTED, OFFLINE }

    private Long id;
    private String eventId;
    private String chargerId;
    private Status status;
    private double kwh;
    private Instant eventTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getChargerId() { return chargerId; }
    public void setChargerId(String chargerId) { this.chargerId = chargerId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public double getKwh() { return kwh; }
    public void setKwh(double kwh) { this.kwh = kwh; }
    public Instant getEventTime() { return eventTime; }
    public void setEventTime(Instant eventTime) { this.eventTime = eventTime; }
}
