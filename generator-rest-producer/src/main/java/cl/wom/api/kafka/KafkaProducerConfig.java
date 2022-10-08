package cl.wom.api.kafka;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import cl.wom.api.kafka.dto.Message;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

	/**
	 * Ruta del sevidor kafka.
	 */
	@Value("${kafka.server}")
	private String kafkaServer;

	/**
	 * Protocolo de seguridad para accesar al topico kafka.
	 */
	@Value("${kafka.securityProtocol}")
	private String kafkaSecurityProtocol;

	/**
	 * Ubicacion de truststore para handshake SSL.
	 */
	@Value("${kafka.truststore.location}")
	private String kafkaTruststoreLocation;

	/**
	 * Password asociada a truststore.
	 */
	@Value("${kafka.truststore.pass}")
	private String kafkaTruststorePass;

	@Value("${kafka.maxBlockMsConfig}")
	private Integer kafkaMaxBlockMs;

	@Value("${kafka.requestTimeoutMsConfig}")
	private Integer kafkaRequestTimeoutMs;

	@Value("${kafka.acksConfig}")
	private String kafkaAcksConfig;

	@Value("${kafka.maxInFlightRequestPerConnection}")
	private Integer kafkaMaxInFLightRequestsPerConnection;

	@Value("${kafka.lingerMsConfig}")
	private Integer kafkaLingerMsConfig;

	private Map<String, Object> configProps() {
		Map<String, Object> configProps = new HashMap<>();

		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaMaxBlockMs);
		configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaRequestTimeoutMs);

		configProps.put(ProducerConfig.ACKS_CONFIG, kafkaAcksConfig);
		configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaMaxInFLightRequestsPerConnection);
		configProps.put(ProducerConfig.LINGER_MS_CONFIG, kafkaLingerMsConfig);

		configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		configProps.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);

		configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol);
		configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaTruststoreLocation);
		configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
				new String(Base64.getDecoder().decode(kafkaTruststorePass)));

		return configProps;
	}

	@Bean
	ProducerFactory<String, Message> producerFactory() {
		DefaultKafkaProducerFactory<String, Message> factory = new DefaultKafkaProducerFactory<>(configProps());
		factory.setTransactionIdPrefix("KAF-" + UUID.randomUUID().toString());
		return factory;
	}

	@Bean
	KafkaTemplate<String, Message> producerKafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean(name = "ktm")
	KafkaTransactionManager<String, Message> kafkaTransactionManager() {
		KafkaTransactionManager<String, Message> ktm = new KafkaTransactionManager<>(producerFactory());
		ktm.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
		return ktm;
	}
}
