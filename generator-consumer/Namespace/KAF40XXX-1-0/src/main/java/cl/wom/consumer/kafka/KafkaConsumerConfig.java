package cl.wom.consumer.kafka;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import cl.wom.consumer.kafka.to.Message;

@Configuration
public class KafkaConsumerConfig {

	@Value("${kafka.server}")
	private String kafkaServer;

	@Value("${kafka.groupId}")
	private String kafkaGroupId;

	@Value("${kafka.concurrency}")
	private Integer concurrency;

	@Value("${kafka.requestTimeoutMs}")
	private Integer kafkaRequestTimeoutMs;

	@Value("${kafka.maxPollRecords}")
	private Integer kafkaMaxPollRecords;

	@Value("${kafka.heartbeatIntervalMs}")
	private Integer kafkaHeartbeatIntervalMs;

	@Value("${kafka.maxPollIntervalMs}")
	private Integer kafkaMaxPollIntervalMs;

	@Value("${kafka.sessionTimeoutMs}")
	private Integer kafkaSessionTimeoutMs;

	@Value("${kafka.securityProtocol}")
	private String kafkaSecurityProtocol;

	@Value("${kafka.truststore.location}")
	private String kafkaTruststoreLocation;

	@Value("${kafka.truststore.pass}")
	private byte[] kafkaTruststorePassword;

	@Bean
	ConsumerFactory<String, Message> consumerFactory() {
		Map<String, Object> consumerProps = new HashMap<>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
		consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		consumerProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaRequestTimeoutMs);
		consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaMaxPollRecords);
		consumerProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaHeartbeatIntervalMs);
		consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaMaxPollIntervalMs);
		consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaSessionTimeoutMs);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		consumerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol);
		consumerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaTruststoreLocation);
		consumerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
				new String(Base64.getDecoder().decode(kafkaTruststorePassword)));

		JsonDeserializer<Message> jsonDeserializer = new JsonDeserializer<>(Message.class, false);
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), jsonDeserializer);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties().setSyncCommits(true);
		factory.setConcurrency(concurrency);
		return factory;
	}

}
