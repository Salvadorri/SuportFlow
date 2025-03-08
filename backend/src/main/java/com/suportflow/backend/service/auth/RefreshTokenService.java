// src/main/java/com/suportflow/backend/service/auth/RefreshTokenService.java

package com.suportflow.backend.service.auth;

import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.RefreshTokenRepository;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpiration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(Long entityId) { // Receives the entity ID (User or Cliente)
        RefreshToken refreshToken = new RefreshToken();

        // Try to find a user with the given ID
        Optional<User> userOptional = userRepository.findById(entityId);
        if (userOptional.isPresent()) {
            refreshToken.setUser(userOptional.get());
        } else {
            // If no user is found, try to find a client
            Optional<Cliente> clienteOptional = clienteRepository.findById(entityId);
            if (clienteOptional.isPresent()) {
                refreshToken.setCliente(clienteOptional.get());
            } else {
                // If neither user nor client is found, throw an exception
                throw new RuntimeException("No user or client found with ID: " + entityId);
            }
        }

        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(refreshTokenRepository::deleteByUser);
    }
    @Transactional
    public void deleteByClienteId(Long clienteId) {
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        cliente.ifPresent(refreshTokenRepository::deleteByCliente);

    }
}