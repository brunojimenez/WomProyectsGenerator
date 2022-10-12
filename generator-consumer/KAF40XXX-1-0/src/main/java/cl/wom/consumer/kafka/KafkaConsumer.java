package cl.wom.consumer.kafka;

import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import cl.wom.consumer.kafka.to.Message;
import cl.wom.consumer.service.ConsumerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaConsumer {

	private ConsumerService service;

	@KafkaListener(groupId = "${kafka.groupId}", topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
	public void listen(ConsumerRecord<String, Message> consumerRecord, Acknowledgment ack) throws Exception {

		log.info("[listen] Partition = {}, Offset = {}", consumerRecord.partition(), consumerRecord.offset());

		try {

			// Set trace id to logger
			MDC.put("trace-id", UUID.randomUUID().toString().substring(0, 8));

			Object value = consumerRecord.value();

			if (value instanceof Message) {
				Message data = (Message) value;
				service.process(data);
			} else {
				log.info("[listen] Mensaje ignorado por casteo");
			}

		} catch (Exception e) {
			log.error("[listen] Error= {}", e.getMessage());
		} finally {
			ack.acknowledge();
			MDC.clear();
		}

	}

}
