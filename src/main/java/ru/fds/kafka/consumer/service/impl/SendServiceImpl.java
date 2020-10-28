package ru.fds.kafka.consumer.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.fds.kafka.consumer.Constants;
import ru.fds.kafka.consumer.service.SendService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SendServiceImpl implements SendService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final Constants constants;

    public SendServiceImpl(KafkaTemplate<String, byte[]> kafkaTemplate,
                           Constants constants) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }

    @Override
    public void sendFileAnswer0(Path fileLocation) throws IOException {
        byte[] bytes = Files.readAllBytes(fileLocation);
        kafkaTemplate.send(constants.getTopicNameAnswer0(), fileLocation.getFileName().toString(), bytes);
    }
}
