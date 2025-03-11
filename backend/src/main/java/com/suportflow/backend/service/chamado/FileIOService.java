package com.suportflow.backend.service.chamado;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileIOService {
    void copy(InputStream source, Path target) throws IOException;
    void createDirectories(Path dir) throws IOException;
}