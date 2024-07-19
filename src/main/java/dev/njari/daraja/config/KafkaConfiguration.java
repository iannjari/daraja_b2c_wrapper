package dev.njari.daraja.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;

/**
 * @author njari_mathenge
 * on 19/07/2024.
 * github.com/iannjari
 */

@Configuration
public class KafkaConfiguration {

    private final ProducerFactory<String, ?> kafkaProducerFactory;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public KafkaConfiguration(ProducerFactory<String, ?> kafkaProducerFactory) {
        this.kafkaProducerFactory = kafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        var configs = new HashMap<>(kafkaProducerFactory.getConfigurationProperties());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configs));
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaManualACKListenerByteArrayContainerFactory(
            ConsumerFactory<String, ?> kafkaConsumerFactory) {
        var configs = new HashMap<>(kafkaConsumerFactory.getConfigurationProperties());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        var bytesConsumerFactory = new DefaultKafkaConsumerFactory<>(configs);
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bytesConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
