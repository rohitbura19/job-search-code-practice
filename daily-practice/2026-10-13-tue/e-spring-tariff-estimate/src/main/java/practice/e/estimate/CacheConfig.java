package practice.e.estimate;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/** GIVEN. Turns the @Cacheable / @CacheEvict proxies on (Boot supplies a simple in-memory CacheManager; Caffeine is a YAML exercise on paper). */
@Configuration
@EnableCaching
public class CacheConfig {}
