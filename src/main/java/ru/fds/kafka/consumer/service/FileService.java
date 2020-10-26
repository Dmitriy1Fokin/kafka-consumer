package ru.fds.kafka.consumer.service;

import java.io.IOException;

public interface FileService {
    String saveFile(byte[] file, String fileName) throws IOException;
}
