package hu.rhykee.deaths_coffer_calculator.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "app")
@Component
@Data
@NoArgsConstructor
public class AppConfig {

    private String jagexPriceUrl;
    private String wikiPriceUrl;
    private String wikiItemInfoUrl;
    private String wikiTradeInfoUrl;

}
