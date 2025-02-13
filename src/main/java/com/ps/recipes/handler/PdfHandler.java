package com.ps.recipes.handler;

import com.ps.recipes.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PdfHandler implements FileHandler {

    private final FileStorageService fileStorageService;

    @Autowired
    public PdfHandler(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public String getSupportedFileType() {
        return "application/pdf";
    }

    @Override
    public void handle(MultipartFile file) throws IOException {
        fileStorageService.saveFile(file.getBytes(), "pdf", "pdf");
    }
}
