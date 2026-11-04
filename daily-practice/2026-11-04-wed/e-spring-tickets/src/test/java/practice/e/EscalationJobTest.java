package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.annotation.Scheduled;

class EscalationJobTest {
    final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withBean(TicketService.class, () -> mock(TicketService.class))
            .withUserConfiguration(EscalationJob.class);

    @Test void absentPropertyMeansNoJob() {
        runner.run(ctx -> assertThat(ctx).doesNotHaveBean(EscalationJob.class));
    }

    @Test void falseMeansNoJob() {
        runner.withPropertyValues("tickets.escalation.enabled=false").run(ctx -> assertThat(ctx).doesNotHaveBean(EscalationJob.class));
    }

    @Test void trueRegistersTheJobWhichCallsTheService() {
        runner.withPropertyValues("tickets.escalation.enabled=true").run(ctx -> {
            assertThat(ctx).hasSingleBean(EscalationJob.class);
            ctx.getBean(EscalationJob.class).run();
            verify(ctx.getBean(TicketService.class)).escalateStale();
        });
    }

    @Test void runIsScheduledWithAConfigurableCron() throws Exception {
        Scheduled s = EscalationJob.class.getMethod("run").getAnnotation(Scheduled.class);
        assertThat(s).isNotNull();
        assertThat(s.cron()).isEqualTo("${tickets.escalation.cron:0 0 * * * *}");
    }
}
