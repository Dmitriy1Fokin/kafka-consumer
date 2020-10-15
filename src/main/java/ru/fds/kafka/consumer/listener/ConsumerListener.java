package ru.fds.kafka.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(topics = "#{constants.topicNameSimple}", groupId = "#{constants.groupNameSimple}")
    public void listenGroupFoo(@Payload String message,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("Received Message in group {}: {}. Partition: {}", constants.getGroupNameSimple(), message, partition);
    }


}
