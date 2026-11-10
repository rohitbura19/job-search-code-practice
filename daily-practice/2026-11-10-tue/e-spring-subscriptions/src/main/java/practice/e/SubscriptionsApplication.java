package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SubscriptionsApplication {
    public static void main(String[] args) { SpringApplication.run(SubscriptionsApplication.class, args); }

    @Bean Clock clock() { return Clock.systemUTC(); }
}
