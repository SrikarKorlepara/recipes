package com.ps.recipes.handler;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileHandler {
    String getSupportedFileType();
    void handle(MultipartFile file) throws IOException;
}
