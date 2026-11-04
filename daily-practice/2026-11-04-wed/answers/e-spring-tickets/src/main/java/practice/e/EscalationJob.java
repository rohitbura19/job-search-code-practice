package practice.e;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Off unless switched on per environment. In a multi-instance deployment every node would run it: add a distributed lock (ShedLock) or run it on one node only. */
@Component
@ConditionalOnProperty(name = "tickets.escalation.enabled", havingValue = "true")
public class EscalationJob {
    private final TicketService service;

    public EscalationJob(TicketService service) { this.service = service; }

    @Scheduled(cron = "${tickets.escalation.cron:0 0 * * * *}")
    public void run() { service.escalateStale(); }
}
