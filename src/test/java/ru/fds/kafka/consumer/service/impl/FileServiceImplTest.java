package ru.fds.kafka.consumer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fds.kafka.consumer.service.FileService;
import ru.fds.kafka.consumer.service.ZipService;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileServiceImplTest {

    @Autowired
    FileService fileService;
    @Autowired
    ZipService zipService;

    @Test
    void saveFile() throws IOException {
        String fileName = "fileName";
        Path file = Files.createFile(Paths.get(fileName));
        byte[] bytes = Files.readAllBytes(file);

        String newFileName = "newFileName";
        Path newFile = fileService.saveFile(bytes, newFileName);

        assertEquals(FileChannel.open(newFile).size(), FileChannel.open(file).size());

        Files.deleteIfExists(file);
        Files.deleteIfExists(newFile);

    }

    @Test
    void createFileFromZip() throws IOException {
        Path file1 = Files.createFile(Path.of("REQUEST_1"));
        Path file2 = Files.createFile(Path.of("REQUEST_2"));
        List<Path> listFiles = List.of(file1, file2);

        Path zipFile = zipService.zipFiles(listFiles, "REQUEST_zipFile.zip");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);

        Optional<Path> newZipFile = fileService.createFileFromZip(zipFile.toAbsolutePath());

        assertTrue(newZipFile.isPresent());

        Files.deleteIfExists(zipFile);
        Files.deleteIfExists(Path.of("ANSWER_1"));
        Files.deleteIfExists(Path.of("ANSWER_2"));
        Files.deleteIfExists(newZipFile.get());
    }
}