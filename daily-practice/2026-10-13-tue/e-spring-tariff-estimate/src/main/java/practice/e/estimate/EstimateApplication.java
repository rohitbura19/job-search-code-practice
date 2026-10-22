package practice.e.estimate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** GIVEN. (No @ConfigurationPropertiesScan on purpose: it also runs in @WebMvcTest slices. FxConfig enables FxProps instead.) */
@SpringBootApplication
public class EstimateApplication {
    public static void main(String[] args) { SpringApplication.run(EstimateApplication.class, args); }
}
