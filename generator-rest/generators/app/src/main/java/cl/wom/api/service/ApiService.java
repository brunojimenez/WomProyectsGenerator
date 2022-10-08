package cl.wom.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;
import cl.wom.api.exception.BusinessException;
import cl.wom.api.external.publicapis.dto.PublicapisResponse;
import cl.wom.api.persistence.MongoDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiService {

	private MongoDAO mongoDAO;
	private RetryTemplate retryTemplate;
	private RestTemplate restTemplate;

	public ApiService(MongoDAO mongoDAO, RetryTemplate retryTemplate, RestTemplate restTemplate) {
		this.mongoDAO = mongoDAO;
		this.retryTemplate = retryTemplate;
		this.restTemplate = restTemplate;
	}

	// Read method
	public GetResponse getProcess() {
		log.info("[getProcess] Begin");

		GetResponse getResponse = new GetResponse();
		getResponse.setProcessStatus("OK");

		// MongoDB example
		try {

			mongoDAO.methodWithRepository();
			mongoDAO.methodWithMongoTemplate(null, null, null, null);

		} catch (Exception e) {
			log.error("[getProcess] Error={}", e.getMessage());
			throw new BusinessException("MONGO", "Mongo Error");
		}

		// RestTemplate example
		try {
			PublicapisResponse responseCall = restTemplate.getForObject("https://api.publicapis.org/random",
					PublicapisResponse.class);
			log.error("[getProcess] restTemplate getForObject={}", responseCall);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			log.error("[getProcess] Error={}", e.getResponseBodyAsString());
			throw new BusinessException("REST_CALL", "Rest Call Error");
		}

		// RetryTemplate example
		try {
			log.info("[getProcess] Retry Begin");

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

			log.info("[getProcess] Retry End");
		} catch (Exception e) {
			log.error("[getProcess] Error={}", e.getMessage());
			throw new BusinessException("RETRY", "Retry Error");
		}

		return getResponse;

	}

	// Create method
	public PostResponse postProcess() {
		log.info("[postProcess] Begin");

		PostResponse postResponse = new PostResponse();
		postResponse.setProcessStatus("OK");

		return postResponse;
	}

	// Update method
	public String putProcess() {
		log.info("[putProcess] Begin");
		return "Hello";
	}

	// Delete method don't have response
	public void deleteProcess() {
		log.info("[deleteProcess] Begin");
	}

}
