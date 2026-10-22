package practice.e;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

/** GIVEN. (@WebMvcTest does not scan @Configuration classes, so the web tests never see this.) */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(CommandProps.class)
public class AppConfig {
    @Bean
    Clock clock() { return Clock.systemUTC(); }
}
