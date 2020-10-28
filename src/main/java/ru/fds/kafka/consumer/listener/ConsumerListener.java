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
import ru.fds.kafka.consumer.dto.Message;
import ru.fds.kafka.consumer.service.SendService;
import ru.fds.kafka.consumer.service.impl.FileServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Component
public class ConsumerListener {

    private final Constants constants;
    private final FileServiceImpl fileService;
    private final SendService sendService;

    public ConsumerListener(Constants constants,
                            FileServiceImpl fileService,
                            SendService sendService) {
        this.constants = constants;
        this.fileService = fileService;
        this.sendService = sendService;
    }

    @KafkaListener(topics = {"#{constants.topicNameSimple}"}, groupId = "#{constants.groupNameSimple}")
    public void listenSimpleTopic(@Payload String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName) {
        log.info("listenSimpleTopic. Received Message from topic: {}, in group: {}. Message: {}.   Partition: {}",
                topicName, constants.getGroupNameSimple(), message, partition);
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
        log.info("listenTopicWithCallback. Received Message from topic: {}, in group: {}. Message: {}.   Partition: {}",
                topicName, constants.getGroupNameSimple(), message, partition);
    }

    @KafkaListener(topics = "#{constants.topicNameFilter}",
            containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(Integer message) {
        log.info("listenWithFilter. Received Message in filtered listener: {}", message);
    }

    @KafkaListener(topics = "#{constants.topicNameObject}",
            containerFactory = "messageKafkaListenerContainerFactory")
    public void listenMessageObject(Message message) {
        log.info("listenMessageObject. Received Message in filtered listener: {}", message);
    }

    @KafkaListener(topics = "#{constants.topicNameStreamTable}",
            groupId = "#{constants.groupNameSimple}",
            containerFactory = "longKafkaListenerContainerFactory")
    public void listenStreamTable(Long message,
                                  @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName) {
        log.info("listenStreamTable. Received Message from topic: {}, in group: {}. Key: {}, Message: {}. Partition: {}",
                topicName, constants.getGroupNameSimple(),key, message, partition);
    }

    @KafkaListener(topics = {"#{constants.topicNameFile}"},
            containerFactory = "fileKafkaListenerContainerFactory")
    public void listenFile(byte[] file,
                           @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName){

        Path fileLocation;
        try {
            fileLocation = fileService.saveFile(file, key);

            log.info("listenFile. Received Message from topic: {}, in group: {}. Key: {}, File: {}",
                    topicName, constants.getGroupNameSimple(), key, fileLocation);

        }catch (IOException e){
            log.error("listenFile. Error message:{}",e.getMessage());
        }
    }

    @KafkaListener(topics = {"#{constants.topicNameRequest0}"},
            containerFactory = "fileKafkaListenerContainerFactory")
    public void listenRequest0(byte[] file,
                              @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                              @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName){

        Path fileLocation;
        try {
            String fileName = constants.getWorkDir() + File.separator + key;
            fileLocation = fileService.saveFile(file, fileName);
            log.info("listenFile. Received Message from topic: {}, in group: {}. Key: {}, File: {}",
                    topicName, constants.getGroupNameSimple(), key, fileLocation.getFileName().toString());

        }catch (IOException e){
            log.error("listenFile. Error message:{}",e.getMessage());
            return;
        }


        Optional<Path> answerPath = fileService.createFileFromZip(fileLocation);
        if(answerPath.isPresent()){
            try {
                sendService.sendFileAnswer0(answerPath.get());
                log.info("Send message:{}", answerPath.get().getFileName().toString());
                Path synFile = Path.of(answerPath.get().toAbsolutePath().toString().replace("xml.zip", "syn"));
                sendService.sendFileAnswer0(Files.createFile(synFile));
                log.info("Send message:{}", synFile.getFileName().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
