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
    private ClienteRepository clienteRepository; // Importante

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(Long id) { // Recebe o ID
        RefreshToken refreshToken = new RefreshToken();

        // Tenta encontrar um usuário com o ID fornecido
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            refreshToken.setUser(userOptional.get());
        } else {
            // Se não encontrou um usuário, tenta encontrar um cliente
            Optional<Cliente> clienteOptional = clienteRepository.findById(id);
            if (clienteOptional.isPresent()) {
                refreshToken.setCliente(clienteOptional.get());
            } else {
                // Se nem usuário nem cliente forem encontrados, lança uma exceção
                throw new RuntimeException("Nenhum usuário ou cliente encontrado com o ID: " + id);
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
        Optional<Cliente> cliente = clienteRepository.findById(clienteId); //Verifica se existe o cliente
        cliente.ifPresent(refreshTokenRepository::deleteByCliente); // Deleta usando o método do repositório

    }
}