package hu.rhykee.deaths_coffer_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableMongoRepositories
@EnableScheduling
public class DeathsCofferCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeathsCofferCalculatorApplication.class, args);
	}

}
