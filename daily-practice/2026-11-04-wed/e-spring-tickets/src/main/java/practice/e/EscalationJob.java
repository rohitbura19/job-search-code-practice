package practice.e;

// TODO: a scheduled job that is only a bean when the property tickets.escalation.enabled is "true" (absent = no bean).
//  @Component + @ConditionalOnProperty(name = "tickets.escalation.enabled", havingValue = "true");
//  constructor takes TicketService; method run() is @Scheduled(cron = "${tickets.escalation.cron:0 0 * * * *}") and calls service.escalateStale().
//  (@EnableScheduling is already on the application class.)
public class EscalationJob {
    public EscalationJob(TicketService service) { throw new UnsupportedOperationException("TODO"); }

    public void run() { throw new UnsupportedOperationException("TODO"); }
}
