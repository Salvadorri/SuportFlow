package com.suportflow.backend.repository;

import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.model.Cliente; // Importante adicionar
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // Métodos para User
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);

    // Métodos para Cliente (Adicionar estes)
    Optional<RefreshToken> findByCliente(Cliente cliente);
    void deleteByCliente(Cliente cliente);
}