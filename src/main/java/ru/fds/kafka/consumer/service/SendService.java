package ru.fds.kafka.consumer.service;

import java.io.IOException;
import java.nio.file.Path;

public interface SendService {
    void sendFileAnswer0(Path fileLocation) throws IOException;
}
