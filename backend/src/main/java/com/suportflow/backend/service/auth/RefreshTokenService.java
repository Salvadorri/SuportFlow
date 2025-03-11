package com.suportflow.backend.service.auth;

import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.RefreshTokenRepository;
import com.suportflow.backend.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

  @Value("${jwt.refreshExpiration}")
  private Long refreshTokenDurationMs;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ClienteRepository clienteRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

    @Transactional
  public RefreshToken createRefreshToken(Long entityId) {
    RefreshToken refreshToken = null;

    // Try to find a user with the given ID
    Optional<User> userOptional = userRepository.findById(entityId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      // Check if a refresh token ALREADY exists for this user
      refreshToken = refreshTokenRepository.findByUser(user).orElse(new RefreshToken()); // **THE PROBLEM LINE**
      refreshToken.setUser(user);
    } else {
      // If no user is found, try to find a client
      Optional<Cliente> clienteOptional = clienteRepository.findById(entityId);
      if (clienteOptional.isPresent()) {
        Cliente cliente = clienteOptional.get();
        // Check if a refresh token ALREADY exists for this client
        refreshToken = refreshTokenRepository.findByCliente(cliente).orElse(new RefreshToken());
        refreshToken.setCliente(cliente);
      } else {
        // If neither user nor client is found, throw an exception
        throw new RuntimeException("No user or client found with ID: " + entityId);
      }
    }

    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken = refreshTokenRepository.save(refreshToken); //save will perform an update if the entity already exists.
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(
          token.getToken(),
          "Refresh token was expired. Please make a new signin request");
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