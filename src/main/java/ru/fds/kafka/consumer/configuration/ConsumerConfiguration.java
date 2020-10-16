package ru.fds.kafka.consumer.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.fds.kafka.consumer.Constants;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {

    private final Constants constants;

    public ConsumerConfiguration(Constants constants) {
        this.constants = constants;
    }

    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, constants.getBootstrapAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    public ConsumerFactory<String, Integer> consumerIntegerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, constants.getBootstrapAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> singleContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(constants.getGroupNameSimple()));
        factory.setBatchListener(false);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> twoPartitionContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(constants.getGroupNameTwoPartitions()));
        factory.setBatchListener(false);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Integer> filterKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Integer> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerIntegerFactory(constants.getGroupNameFilter()));
        factory.setRecordFilterStrategy(consumerRecord -> consumerRecord.value() < 0 || consumerRecord.value() > 10);
        return factory;
    }
}
