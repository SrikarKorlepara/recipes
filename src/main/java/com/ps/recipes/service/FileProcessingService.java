package com.ps.recipes.service;

import com.ps.recipes.exception.UnsupportedFileTypeException;
import com.ps.recipes.handler.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {

    private final Map<String, FileHandler> handlers;

    @Autowired
    public FileProcessingService(List<FileHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(FileHandler::getSupportedFileType, Function.identity()));
    }

    public void processFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new UnsupportedFileTypeException("File type could not be determined");
        }

        FileHandler handler = handlers.get(contentType);
        if (handler == null) {
            throw new UnsupportedFileTypeException("Unsupported file type: " + contentType);
        }

        handler.handle(file);
    }
}