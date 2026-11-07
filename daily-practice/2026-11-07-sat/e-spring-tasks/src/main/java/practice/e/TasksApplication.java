package practice.e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TasksApplication {
    public static void main(String[] args) { SpringApplication.run(TasksApplication.class, args); }
}
