package ru.fds.kafka.consumer.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.fds.kafka.consumer.Constants;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfiguration {

    private final Constants constants;

    public ProducerConfiguration(Constants constants) {
        this.constants = constants;
    }

    @Bean
    public KafkaTemplate<String, byte[]> fileKafkaTemplate(){
        return new KafkaTemplate<>(fileProducerFactory());
    }

    public ProducerFactory<String, byte[]> fileProducerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, constants.getBootstrapAddress());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
