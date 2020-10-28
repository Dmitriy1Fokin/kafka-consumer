package ru.fds.kafka.consumer.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface FileService {
    Path saveFile(byte[] file, String fileName) throws IOException;
    Optional<Path> createFileFromZip(Path filePath);
}
