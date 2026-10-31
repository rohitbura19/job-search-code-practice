package practice.e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NotesProps.class)
public class NotesApplication {
    public static void main(String[] args) { SpringApplication.run(NotesApplication.class, args); }
}
