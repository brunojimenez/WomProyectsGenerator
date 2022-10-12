package cl.wom.api.service;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;
import cl.wom.api.exception.BusinessException;
import cl.wom.api.external.publicapis.to.PublicapisResponse;
import cl.wom.api.kafka.KafkaProducer;
import cl.wom.api.kafka.to.Message;
import cl.wom.api.persistence.MongoDAO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ApiService implements ApiServiceInterface {

	private MongoDAO mongoDAO;
	private KafkaProducer kafkaProducer;
	private RetryTemplate retryTemplate;
	private RestTemplate restTemplate;

	// Read method
	public GetResponse getProcess() {
		log.info("Begin");

		GetResponse getResponse = new GetResponse();
		getResponse.setProcessStatus("OK");

		// Example kafka producer
		kafkaProducer.produce(Message.builder().data(MDC.get("trace-id") + "-Hola").build());

		// MongoDB example
		try {

			mongoDAO.methodWithRepository();
			mongoDAO.methodWithMongoTemplate(null, null, null, null);

		} catch (Exception e) {
			log.error("Error={}", e.getMessage());
			throw new BusinessException("MONGO", "Mongo Error");
		}

		// RestTemplate example
		try {
			PublicapisResponse responseCall = restTemplate.getForObject("https://api.publicapis.org/random",
					PublicapisResponse.class);
			log.error("restTemplate getForObject={}", responseCall);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			log.error("Error={}", e.getResponseBodyAsString());
			throw new BusinessException("REST_CALL", "Rest Call Error");
		}

		// RetryTemplate example
		try {
			log.info("Retry Begin");

			retryTemplate.execute(retry -> {
				log.info("[getProcess] RetryCount={}", retry.getRetryCount());

				// Firts intent fail
				if (retry.getRetryCount() == 0)
					throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY);

				// Second intent fail
				if (retry.getRetryCount() == 1)
					throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY);

				// Third intent success
				return true;
			});

			log.info("Retry End");
		} catch (Exception e) {
			log.error("[getProcess] Error={}", e.getMessage());
			throw new BusinessException("RETRY", "Retry Error");
		}

		return getResponse;
	}

	// Create method
	public PostResponse postProcess() {
		log.info("Begin");

		PostResponse postResponse = new PostResponse();
		postResponse.setProcessStatus("OK");

		return postResponse;
	}

	// Update method
	public String putProcess() {
		log.info("Begin");
		return "Hello";
	}

	// Delete method don't have response
	public void deleteProcess() {
		log.info("Begin");
	}

}
