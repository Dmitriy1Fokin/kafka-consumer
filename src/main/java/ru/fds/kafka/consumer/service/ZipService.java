package ru.fds.kafka.consumer.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ZipService {
    Path zipFile(Path filePath, String zipName) throws IOException;
    Path zipFiles(List<Path> filesPath, String zipName) throws IOException;
    List<Path> unZip(Path zipPath, Path unzipPath) throws IOException;
}
