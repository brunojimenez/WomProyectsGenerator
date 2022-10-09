package cl.wom.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Configuration
public class RetryTemplateConfig {

	@Value("${retry.backoff}")
	private long retryBackoff;

	@Value("${retry.retries}")
	private int retryRetries;

	@Bean
	RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(retryBackoff);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		Map<Class<? extends Throwable>, Boolean> supportedExceptions = new HashMap<>();

		// TODO: Register all exception to catch with the retry policy
		supportedExceptions.put(ResourceAccessException.class, true);
		supportedExceptions.put(HttpClientErrorException.class, true);
		supportedExceptions.put(HttpServerErrorException.class, true);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(retryRetries, supportedExceptions);
		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}

}
