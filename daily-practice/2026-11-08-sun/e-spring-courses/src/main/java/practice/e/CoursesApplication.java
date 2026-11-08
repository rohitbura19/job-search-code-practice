package practice.e;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoursesApplication {
    public static void main(String[] args) { SpringApplication.run(CoursesApplication.class, args); }

    /** Injected so tests can freeze "today". */
    @Bean Clock clock() { return Clock.systemDefaultZone(); }
}
