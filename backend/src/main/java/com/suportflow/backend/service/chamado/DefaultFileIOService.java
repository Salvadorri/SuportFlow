package com.suportflow.backend.service.chamado;

import com.suportflow.backend.service.chamado.FileIOService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DefaultFileIOService implements FileIOService {
    @Override
    public void copy(InputStream source, Path target) throws IOException {
        Files.copy(source, target);
    }

    @Override
    public void createDirectories(Path dir) throws IOException {
        Files.createDirectories(dir);
    }
}