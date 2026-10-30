package practice.e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StockProps.class)
public class StockApplication {
    public static void main(String[] args) { SpringApplication.run(StockApplication.class, args); }
}
