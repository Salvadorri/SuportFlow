package com.suportflow.backend.service.auth;

import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.RefreshTokenDTO;
import com.suportflow.backend.dto.UserLoginDTO;
import com.suportflow.backend.exception.TokenRefreshException;
import com.suportflow.backend.model.RefreshToken;
import com.suportflow.backend.model.User;
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserManagementService userService; //Permanece para buscar user
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Transactional // Importante para garantir atomicidade da operação
    public AuthenticationResponse authenticateAndGenerateToken(UserLoginDTO userLoginDTO) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );

        } catch (BadCredentialsException e) {
            // Exceção já tratada no Controller, mas ainda é bom registrar em log
            throw new BadCredentialsException("Credenciais inválidas", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        User user = userService.findByEmail(userLoginDTO.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthenticationResponse(jwt, refreshToken.getToken());
    }


    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {
          String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String token = jwtUtil.generateToken(userDetails);
                    return new AuthenticationResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token inválido ou não encontrado."));
    }
}