package practice.e.estimate;

import org.springframework.context.annotation.Configuration;

/**
 * TODO - on paper or here (no test): @EnableConfigurationProperties(FxProps.class) and a @Bean RestClient fxRestClient(FxProps p)
 * built with RestClient.builder().baseUrl(p.baseUrl()).requestFactory(f), where f is a SimpleClientHttpRequestFactory with
 * connect and read timeout = p.timeout().
 */
@Configuration
public class FxConfig {
    // TODO
}
