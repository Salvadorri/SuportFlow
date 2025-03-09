//src/main/java/com/suportflow/backend/service/user/UserManagementService.java
package com.suportflow.backend.service.user;

import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.EmpresaRepository;
import com.suportflow.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private EmpresaRepository empresaRepository;

    @Transactional
    public UserDetailsDTO registerNewUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
        }

        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

        Empresa empresa = null;
        if (registrationDTO.getEmpresaNome() != null) {
            empresa =
                    empresaRepository
                            .findByNome(registrationDTO.getEmpresaNome())
                            .orElseThrow(
                                    () -> new RuntimeException("Empresa não encontrada: " + registrationDTO.getEmpresaNome()));
        }

        User user = new User();
        user.setNome(registrationDTO.getNome());
        user.setEmail(registrationDTO.getEmail());
        user.setSenha(encodedPassword); // Use the encoded password
        user.setEmpresa(empresa);
        user.setAtivo(true);
        user.setDataCriacao(LocalDateTime.now());

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.", e);
        }

        return new UserDetailsDTO(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o email: " + email));
    }

    @Transactional(readOnly = true)
    public UserDetailsDTO findDTOByEmail(String email) {
        User user = findByEmail(email); // Reuse the existing method
        return new UserDetailsDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDetailsDTO findDTOById(Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + id));
        return new UserDetailsDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDetailsDTO> findAllUsers() {
        return userRepository.findAll().stream().map(UserDetailsDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public UserDetailsDTO updateUserByEmail(String email, UserRegistrationDTO userDTO) {
        User user = findByEmail(email); // Use findByEmail for consistency

        // Update only the fields that are allowed to be changed.  *Don't* update the ID.
        user.setNome(userDTO.getNome());
        user.setEmail(
                userDTO.getEmail()); // Potentially dangerous; consider separate email change flow

        if (userDTO.getEmpresaNome() != null) {
            Empresa empresa =
                    empresaRepository
                            .findByNome(userDTO.getEmpresaNome())
                            .orElseThrow(
                                    () -> new RuntimeException("Empresa não encontrada: " + userDTO.getEmpresaNome()));
            user.setEmpresa(empresa);
        }
        // *Don't* update the password here; use changePassword
        user = userRepository.save(user);
        return new UserDetailsDTO(user);
    }

    @Transactional
    public void changePassword(String email, PasswordChangeDTO passwordChangeDTO) {
        User user = findByEmail(email);

        // Verify old password
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getSenha())) {
            throw new IllegalArgumentException("Senha antiga incorreta.");
        }

        // Check if new password and confirmation match
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        // Encode and set the new password
        String encodedNewPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
        user.setSenha(encodedNewPassword);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + userId));

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<String> getCurrentUserPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptyList();
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}