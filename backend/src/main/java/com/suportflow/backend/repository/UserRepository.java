package com.suportflow.backend.repository;

import com.suportflow.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo email.
     *
     * @param email O email do usuário a ser buscado.
     * @return O usuário encontrado ou null se nenhum usuário for encontrado com o email especificado.
     */
    User findByEmail(String email);

    // Você pode adicionar outros métodos de consulta personalizados aqui, se necessário.
    // Por exemplo:
    // List<User> findByNomeContaining(String nome);
    // User findByEmpresa(Empresa empresa);
}