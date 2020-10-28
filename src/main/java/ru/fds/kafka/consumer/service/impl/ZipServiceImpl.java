package ru.fds.kafka.consumer.service.impl;

import org.springframework.stereotype.Service;
import ru.fds.kafka.consumer.service.ZipService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class ZipServiceImpl implements ZipService {
    @Override
    public Path zipFile(Path filePath, String zipName) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(zipName);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            FileInputStream fis = new FileInputStream(filePath.toFile())){

            ZipEntry zipEntry = new ZipEntry(filePath.toFile().getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

        }
        return Path.of(zipName);
    }

    @Override
    public Path zipFiles(List<Path> filesPath, String zipName) throws IOException{
        List<String> filesPathString = filesPath.stream()
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());

        try(FileOutputStream fos = new FileOutputStream(zipName);
            ZipOutputStream zipOut = new ZipOutputStream(fos)) {


            for (String srcFile : filesPathString) {
                File fileToZip = new File(srcFile);
                try(FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
            }
        }
        return Path.of(zipName);
    }

    @Override
    public List<Path> unZip(Path zipPath, Path unzipPath) throws IOException {
        List<Path> unzipFiles = new ArrayList<>();

        byte[] buffer = new byte[1024];
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath.toFile()))){
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(unzipPath.toFile(), zipEntry);

                try(FileOutputStream fos = new FileOutputStream(newFile)){
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    unzipFiles.add(Path.of(newFile.getAbsolutePath()));
                    zipEntry = zis.getNextEntry();
                }
            }
            zis.closeEntry();
        }
        return unzipFiles;
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir.getCanonicalPath(), zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
