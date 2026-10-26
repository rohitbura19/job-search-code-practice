package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(RefundProperties.class)
public class RefundApplication {
    public static void main(String[] args) { SpringApplication.run(RefundApplication.class, args); }

    @Bean Clock clock() { return Clock.systemUTC(); }
}
