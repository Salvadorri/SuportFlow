import com.suportflow.backend.controller.FileUploadController;
import com.suportflow.backend.service.chamado.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(FileUploadController.class)
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    void uploadFile_shouldReturnOkAndFilePath() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, world!".getBytes()
        );
        String filePath = "/path/to/uploaded/test.txt";
        when(fileStorageService.storeFile(any())).thenReturn(filePath);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(filePath));
    }

    @Test
    void uploadFile_shouldReturnInternalServerErrorWhenServiceFails() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, world!".getBytes()
        );
        when(fileStorageService.storeFile(any())).thenThrow(new IOException("Simulated error"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }
}