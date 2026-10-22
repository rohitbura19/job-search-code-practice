package practice.e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** GIVEN. Only the tests are meant to run (there is no database or broker behind CommandStore / KafkaTemplate). */
@SpringBootApplication
public class CommandApplication {
    public static void main(String[] args) { SpringApplication.run(CommandApplication.class, args); }
}
