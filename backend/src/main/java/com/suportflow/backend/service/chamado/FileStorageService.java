package com.suportflow.backend.service.chamado;

import com.suportflow.backend.service.chamado.FileIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    private final FileIOService fileIOService;

    @Autowired
    public FileStorageService(FileIOService fileIOService) {
        this.fileIOService = fileIOService;
    }

    public String storeFile(MultipartFile file) throws IOException {
        // Gera um nome de arquivo único para evitar conflitos
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // Cria o diretório se não existir
        fileIOService.createDirectories(Paths.get(uploadDir));

        // Salva o arquivo no sistema de arquivos
        fileIOService.copy(file.getInputStream(), filePath);

        // Retorna o caminho completo do arquivo (você pode querer retornar apenas o nome do arquivo)
        return filePath.toString(); // ou fileName;
    }

    public Path loadFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
}