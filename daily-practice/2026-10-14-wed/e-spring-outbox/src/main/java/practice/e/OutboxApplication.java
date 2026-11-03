package practice.e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** GIVEN. Only here so @WebMvcTest has a configuration to start from; the app is not meant to be run (no database). */
@SpringBootApplication
@EnableScheduling
public class OutboxApplication {
    public static void main(String[] args) { SpringApplication.run(OutboxApplication.class, args); }
}
