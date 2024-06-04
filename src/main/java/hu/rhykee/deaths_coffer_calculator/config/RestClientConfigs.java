package hu.rhykee.deaths_coffer_calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Configuration
public class RestClientConfigs {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> httpHeaders.add(USER_AGENT, "DeathsCofferCalculator"))
                .build();
    }

}
