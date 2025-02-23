package com.suportflow.backend.repository;

import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user); // Adicione esta linha
    void deleteByUser(User user);
}
