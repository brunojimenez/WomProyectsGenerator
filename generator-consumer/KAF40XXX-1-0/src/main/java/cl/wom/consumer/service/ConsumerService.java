package cl.wom.consumer.service;

import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import cl.wom.consumer.exception.BusinessException;
import cl.wom.consumer.external.publicapis.dto.PublicapisResponse;
import cl.wom.consumer.kafka.to.Message;
import cl.wom.consumer.persistence.MongoDAO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ConsumerService {

	private MongoDAO mongoDAO;

	private RetryTemplate retryTemplate;

	private RestTemplate restTemplate;

	public void process(Message message) {
		log.info("[process] Begin");

		log.info("[process] message={}", message);

		// MongoDB example
		try {

			mongoDAO.methodWithRepository();
			mongoDAO.methodWithMongoTemplate(null, null, null, null);

		} catch (Exception e) {
			log.error("[process] Error={}", e.getMessage());
			throw new BusinessException("MONGO", "Mongo Error");
		}

		// RestTemplate example
		try {
			PublicapisResponse responseCall = restTemplate.getForObject("https://api.publicapis.org/random",
					PublicapisResponse.class);
			log.error("[process] restTemplate getForObject={}", responseCall);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			log.error("[process] Error={}", e.getResponseBodyAsString());
			throw new BusinessException("REST_CALL", "Rest Call Error");
		}

		// RetryTemplate example
		try {
			log.info("[process] Retry Begin");

			retryTemplate.execute(retry -> {
				log.info("[process] RetryCount={}", retry.getRetryCount());

				// Firts intent fail
				if (retry.getRetryCount() == 0)
					throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY);

				// Second intent fail
				if (retry.getRetryCount() == 1)
					throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY);

				// Third intent success
				return true;
			});

			log.info("[process] Retry End");
		} catch (Exception e) {
			log.error("[process] Error={}", e.getMessage());
			throw new BusinessException("RETRY", "Retry Error");
		}

		log.info("[process] End");
	}

}
