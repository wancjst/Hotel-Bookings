package com.iquantex.samples.bookings.config;

import com.iquantex.samples.bookings.Service.listener.HotelBookingsEventListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ConditionalOnProperty(value = "event.listener.enabled", havingValue = "true")
public class KafkaConsumerConfiguration {

	@Value("${spring.kafka.bootstrap-servers}")
	private String mqAddress;

	/**
	 * 创建一个kafka的Consumer工厂
	 */
	@Bean
	public ConsumerFactory<String, byte[]> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, mqAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "bank-account-event-listener");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	/**
	 * 创建一个Kafka的listener
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory(
			ConsumerFactory<String, byte[]> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}

	/**
	 * 创建账户事件的listener
	 */
	@Bean
	public HotelBookingsEventListener bankAccountListener() {
		return new HotelBookingsEventListener();
	}

}