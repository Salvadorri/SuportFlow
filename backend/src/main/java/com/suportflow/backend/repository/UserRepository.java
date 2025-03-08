package com.suportflow.backend.repository;

import com.suportflow.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importante!

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // Long, pois o ID é Long

    /**
     * Busca um usuário pelo email.
     *
     * @param email O email do usuário a ser buscado.
     * @return Um Optional contendo o usuário encontrado, ou um Optional vazio se nenhum usuário for encontrado.
     */
    Optional<User> findByEmail(String email); // Retorna Optional<User>

    boolean existsByEmail(String email); // Adicionado

    // Outros métodos de consulta personalizados, se necessário
}