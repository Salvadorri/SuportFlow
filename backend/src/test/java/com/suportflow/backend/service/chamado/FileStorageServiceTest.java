package com.suportflow.backend.service.chamado;

import com.suportflow.backend.service.chamado.FileStorageService;
import com.suportflow.backend.service.chamado.FileIOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    @Mock
    private FileIOService fileIOService; // Mock the interface

    private String uploadDir = "test-uploads";  // Use um diretório de teste

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(fileStorageService, "uploadDir", uploadDir);  // Injeta o valor do uploadDir
    }

    @Test
    void storeFile_shouldSaveFileAndReturnPath() throws IOException {
        // Arrange
        String fileName = "test.txt";
        MultipartFile file = new MockMultipartFile("file", fileName, "text/plain", "Hello, world!".getBytes());

        // Act
        String filePath = fileStorageService.storeFile(file);

        // Assert
        assertNotNull(filePath);
        assertTrue(filePath.contains(fileName));
        assertTrue(java.nio.file.Files.exists(Paths.get(filePath)));

        // Cleanup
        java.nio.file.Files.delete(Paths.get(filePath));
    }


    @Test
    void storeFile_shouldCreateDirectoryIfNotExists() throws IOException {
        // Arrange
        String fileName = "test.txt";
        MultipartFile file = new MockMultipartFile("file", fileName, "text/plain", "Hello, world!".getBytes());

        // Delete the directory if it exists
        Path uploadDirPath = Paths.get(uploadDir);
        if (java.nio.file.Files.exists(uploadDirPath)) {
            java.nio.file.Files.walk(uploadDirPath)
                    .sorted((a, b) -> b.compareTo(a)) // Reverse order for deleting directories with content
                    .forEach(path -> {
                        try {
                            java.nio.file.Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        assertFalse(java.nio.file.Files.exists(uploadDirPath));

        // Act
        String filePath = fileStorageService.storeFile(file);

        // Assert
        assertTrue(java.nio.file.Files.exists(uploadDirPath)); // Verify the directory was created
        assertTrue(java.nio.file.Files.exists(Paths.get(filePath)));

        // Cleanup
        java.nio.file.Files.delete(Paths.get(filePath));
        java.nio.file.Files.delete(uploadDirPath);
    }

    @Test
    void storeFile_shouldThrowIOExceptionWhenFailsToCopy() throws IOException {
        // Arrange
        String fileName = "test.txt";
        MultipartFile file = new MockMultipartFile("file", fileName, "text/plain", "Hello, world!".getBytes());

        // Mock the Files.copy to throw an exception
        doThrow(new IOException("Simulated IO Exception")).when(fileIOService).copy(any(), any());

        // Act and Assert
        assertThrows(IOException.class, () -> fileStorageService.storeFile(file));
    }


}