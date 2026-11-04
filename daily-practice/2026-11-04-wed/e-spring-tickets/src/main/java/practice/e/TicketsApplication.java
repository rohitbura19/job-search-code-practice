package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicketsApplication {
    public static void main(String[] args) { SpringApplication.run(TicketsApplication.class, args); }

    @Bean Clock clock() { return Clock.systemUTC(); }
}
