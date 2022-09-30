package cl.wom.rest.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Value("${service.timeout}")
	private Long serviceTimeout;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, Environment env) {
		serviceTimeout = Long.valueOf(serviceTimeout);

		return builder.setConnectTimeout(Duration.ofMillis(serviceTimeout))
				.setReadTimeout(Duration.ofMillis(serviceTimeout)).build();
	}

}
