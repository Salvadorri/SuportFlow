package com.suportflow.backend.controller;

import com.suportflow.backend.dto.AuthenticationResponse;
import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserLoginDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.model.User;
import com.suportflow.backend.security.JwtUtil;
import com.suportflow.backend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDTO userLoginDTO) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Email ou senha incorretos.", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        UserDetailsDTO registeredUser = userService.registerNewUser(registrationDTO);
        return ResponseEntity.ok(registeredUser);
    }

}