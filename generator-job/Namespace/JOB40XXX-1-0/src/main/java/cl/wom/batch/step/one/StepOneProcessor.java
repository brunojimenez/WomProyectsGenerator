package cl.wom.batch.step.one;

import org.slf4j.MDC;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import cl.wom.batch.exception.BusinessException;
import cl.wom.batch.external.publicapis.dto.PublicapisResponse;
import cl.wom.batch.kafka.KafkaProducer;
import cl.wom.batch.kafka.dto.Message;
import cl.wom.batch.step.one.to.StepOneDataTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class StepOneProcessor implements ItemProcessor<StepOneDataTO, String> {

	private RetryTemplate retryTemplate;
	private RestTemplate restTemplate;
	private KafkaProducer kafkaProducer;

	@Override
	public String process(StepOneDataTO dataTO) throws Exception {

		MDC.put("id", dataTO.getId());

		log.info("[process] data={}", dataTO);

		// Example kafka producer
		kafkaProducer.produce(Message.builder().data(MDC.get("trace-id") + "-Hola").build());

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

		return dataTO.getData();
	}

}
