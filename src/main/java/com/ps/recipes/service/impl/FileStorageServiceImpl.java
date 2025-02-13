package com.ps.recipes.service.impl;

import com.ps.recipes.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void saveFile(byte[] bytes, String fileExtension, String subDirectory) throws IOException {
        String fileName = UUID.randomUUID() + "." + fileExtension;
        Path uploadPath = Paths.get(uploadDir, subDirectory);
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, bytes);
    }
}