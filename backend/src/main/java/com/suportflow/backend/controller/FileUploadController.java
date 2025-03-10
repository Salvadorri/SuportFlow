package com.suportflow.backend.controller;

import com.suportflow.backend.service.chamado.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileStorageService.storeFile(file);
            return ResponseEntity.ok(filePath); // Retorna o caminho do arquivo
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}