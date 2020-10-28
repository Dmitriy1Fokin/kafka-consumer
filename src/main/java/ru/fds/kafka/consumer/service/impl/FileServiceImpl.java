package ru.fds.kafka.consumer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fds.kafka.consumer.service.FileService;
import ru.fds.kafka.consumer.service.ZipService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final ZipService zipService;

    public FileServiceImpl(ZipService zipService) {
        this.zipService = zipService;
    }

    @Override
    public Path saveFile(byte[] file, String fileName) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(file);
        Path fileLocation = Paths.get(fileName);

        try(FileOutputStream fileOutputStream = new FileOutputStream(fileLocation.toFile())) {
            int ch;
            while ((ch = inputStream.read()) != -1) {
                fileOutputStream.write(ch);
            }
        }

        return fileLocation;
    }

    @Override
    public Optional<Path> createFileFromZip(Path filePath){
        if(!getExtension(filePath.getFileName().toString()).equals("zip")) {
            return Optional.empty();
        }

        try {
            Path directoryPath = filePath.getParent();
            List<Path> unzipFiles =  zipService.unZip(filePath, directoryPath);

            List<Path> answerFiles = new ArrayList<>(unzipFiles.size());
            for(Path path : unzipFiles){
                String newName = path.getFileName().toString().replace("REQUEST", "ANSWER");
                answerFiles.add(Files.move(path, Path.of(newName)));
            }

            Path zipAnswer = zipService.zipFiles(answerFiles, filePath.getFileName().toString().replace("REQUEST", "ANSWER"));

            return Optional.of(zipAnswer);

        }catch (IOException e){
            log.error("can't unzip zip:{}", filePath.getFileName().toFile());
            return Optional.empty();
        }

    }

    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = fileName.lastIndexOf(File.separator);

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }


}
