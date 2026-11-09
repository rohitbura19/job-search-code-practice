package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InvoicesApplication {
    public static void main(String[] args) { SpringApplication.run(InvoicesApplication.class, args); }

    @Bean Clock clock() { return Clock.systemUTC(); }
}
