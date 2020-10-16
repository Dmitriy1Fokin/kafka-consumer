package ru.fds.kafka.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.fds.kafka.consumer.Constants;

@Slf4j
@Component
public class ConsumerListener {

    private final Constants constants;

    public ConsumerListener(Constants constants) {
        this.constants = constants;
    }

    @KafkaListener(topics = {"#{constants.topicNameSimple}"}, groupId = "#{constants.groupNameSimple}")
    public void listenSimpleTopic(@Payload String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName) {
        log.info("listenSimpleTopic. Received Message from topic: {}, in group: {}. Message: {}.   Partition: {}", topicName, constants.getGroupNameSimple(), message, partition);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "#{constants.topicNameSimple}",
                                                        partitionOffsets = {
                                                                @PartitionOffset(partition = "1", initialOffset = "0"),
                                                                @PartitionOffset(partition = "3", initialOffset = "0")}),
                groupId = "#{constants.groupNameTwoPartitions}")
    public void listenToPartition(@Payload String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("listenToPartition. Received message: {} from partition: {}", message, partition);
    }

    @KafkaListener(topics = "#{constants.topicNameCallback}", groupId = "#{constants.groupNameSimple}")
    public void listenTopicWithCallback(String message,
                                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName) {
        log.info("listenTopicWithCallback. Received Message from topic: {}, in group: {}. Message: {}.   Partition: {}", topicName, constants.getGroupNameSimple(), message, partition);
    }

    @KafkaListener(topics = "#{constants.topicNameFilter}",
            containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(Integer message) {
        log.info("listenWithFilter. Received Message in filtered listener: {}", message);
    }

}
