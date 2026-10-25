package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(CouponProperties.class)
public class CouponApplication {
    public static void main(String[] args) { SpringApplication.run(CouponApplication.class, args); }

    @Bean Clock clock() { return Clock.systemUTC(); }
}
