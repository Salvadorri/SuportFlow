// src/main/java/com/suportflow/backend/service/chamado/FileIOServiceImpl.java
package com.suportflow.backend.service.chamado;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;

@Service
public class FileIOServiceImpl implements FileIOService {

    @Override
    public void copy(InputStream source, Path target) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void createDirectories(Path dir) throws IOException {
        Files.createDirectories(dir);
    }
}