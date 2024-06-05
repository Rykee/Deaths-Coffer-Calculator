package hu.rhykee.deaths_coffer_calculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Configuration
public class BeanConfigs {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> httpHeaders.add(USER_AGENT, "DeathsCofferCalculator"))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://api.deaths-coffer.com");
        return new OpenAPI().servers(List.of(server));
    }

}
