package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShipmentsApplication {
    public static void main(String[] args) { SpringApplication.run(ShipmentsApplication.class, args); }

    @Bean Clock clock() { return Clock.systemUTC(); }
}
