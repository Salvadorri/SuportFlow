package com.suportflow.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Ativa explicitamente o perfil "test"
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}
}