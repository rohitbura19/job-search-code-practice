package practice.e;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Separate from the application class so @WebMvcTest slices do not need a JPA metamodel. */
@Configuration
@EnableJpaAuditing
public class AuditingConfig {}
