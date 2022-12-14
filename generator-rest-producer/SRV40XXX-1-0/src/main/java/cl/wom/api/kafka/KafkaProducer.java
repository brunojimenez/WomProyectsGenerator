package cl.wom.api.kafka;

import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import cl.wom.api.kafka.to.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaProducer {

	@Value(value = "${kafka.topic}")
	private String topic;

	private final KafkaTemplate<String, Message> kafkaTemplate;

	public KafkaProducer(KafkaTemplate<String, Message> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Transactional("ktm")
	public boolean produce(Message message) {
		log.debug("message={}", message);
		try {
			ListenableFuture<SendResult<String, Message>> future = kafkaTemplate.send(topic, message);

			future.addCallback(new ListenableFutureCallback<SendResult<String, Message>>() {
				@Override
				public void onSuccess(SendResult<String, Message> result) {
					log.info("Message OK");
				}

				@Override
				public void onFailure(Throwable e) {
					log.error("[Throwable] Error={}", e);
					throw new KafkaException(e);
				}
			});

			log.info("Offset={}", future.get().getRecordMetadata().offset());
		} catch (Exception e) {
			log.error("[InterruptedException] Error={}", e);
			throw new KafkaException(e);
		}
		return true;
	}

}
