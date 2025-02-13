package com.ps.recipes.service;

import java.io.IOException;

public interface FileStorageService {


    void saveFile(byte[] file, String fileName,String subDirectory) throws IOException;
}
