package ru.fds.kafka.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fds.kafka.consumer.Constants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final Constants constants;

    public FileServiceImpl(Constants constants) {
        this.constants = constants;
    }

    @Override
    public String saveFile(byte[] file, String fileName) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(file);
        File inputDir = new File(constants.getWorkDir());
        String fileLocation = inputDir + File.separator + fileName;

        try(FileOutputStream fileOutputStream = new FileOutputStream(fileLocation)) {
            int ch;
            while ((ch = inputStream.read()) != -1) {
                fileOutputStream.write(ch);
            }
        }

        return fileLocation;
    }
}
