package ru.fds.kafka.consumer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fds.kafka.consumer.service.ZipService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ZipServiceImplTest {

    @Autowired
    ZipService zipService;




    @Test
    void zipFile() throws IOException {
        String fileTestName = "testFile";
        Path file = Files.createFile(Path.of(fileTestName));

        String zipName = fileTestName + ".zip";
        Path zipPath = zipService.zipFile(file, zipName);

        assertEquals(fileTestName+".zip", zipPath.getFileName().toString());

        Files.deleteIfExists(file);
        Files.deleteIfExists(zipPath);
    }

    @Test
    void zipFiles() throws IOException {
        String fileTestName1 = "testFile1";
        String fileTestName2 = "testFile2";
        Path file1 = Files.createFile(Path.of(fileTestName1));
        Path file2 = Files.createFile(Path.of(fileTestName2));

        String zipName = "zipName.zip";
        Path zipPath = zipService.zipFiles(List.of(file1, file2), zipName);

        assertEquals(zipName, zipPath.getFileName().toString());

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(zipPath);
    }

    @Test
    void unZip() throws IOException {
        String fileTestName1 = "testFile1";
        String fileTestName2 = "testFile2";

        Path file1 = Files.createFile(Path.of(fileTestName1));
        Path file2 = Files.createFile(Path.of(fileTestName2));

        String zipName = "zipName";
        Path zipPath = zipService.zipFiles(List.of(file1, file2), zipName);

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);

        List<Path> unZipFile = zipService.unZip(zipPath, Path.of(""));

        assertEquals(2, unZipFile.size());
        assertEquals(fileTestName1, unZipFile.get(0).getFileName().toString());

        Files.deleteIfExists(zipPath);
        for (Path path : unZipFile){
            Files.deleteIfExists(path);
        }
    }
}