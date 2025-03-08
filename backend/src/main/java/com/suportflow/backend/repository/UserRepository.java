package com.suportflow.backend.repository;

import com.suportflow.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo email.
     *
     * @param email O email do usuário a ser buscado.
     * @return Um Optional contendo o usuário encontrado, ou um Optional vazio se nenhum usuário for encontrado.
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe um usuário com o email informado.
     *
     * @param email O email a ser verificado.
     * @return true se existir um usuário com o email informado, false caso contrário.
     */
    boolean existsByEmail(String email);

    // Outros métodos de consulta personalizados, se necessário
}