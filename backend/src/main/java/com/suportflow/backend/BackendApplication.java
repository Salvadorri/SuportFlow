package com.suportflow.backend; // Exemplo - ajuste para o seu pacote

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.suportflow.backend.model"}) // ONDE SUAS ENTIDADES ESTÃO
@EnableJpaRepositories(basePackages = {"com.suportflow.backend.repository"}) // ONDE SEUS REPOSITÓRIOS ESTÃO
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}