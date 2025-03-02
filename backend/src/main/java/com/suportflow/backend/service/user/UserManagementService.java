package com.suportflow.backend.service.user;

import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.EmpresaRepository;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Transactional
    public UserDetailsDTO registerNewUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
        }

        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

        // Buscar a empresa pelo nome (se fornecido), usando orElseThrow
        Empresa empresa = null;
        if(registrationDTO.getEmpresaNome() != null){
            empresa = empresaRepository.findByNome(registrationDTO.getEmpresaNome())
                    .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + registrationDTO.getEmpresaNome()));
        }


        User user = new User();
        user.setNome(registrationDTO.getNome());
        user.setEmail(registrationDTO.getEmail());
        user.setSenha(encodedPassword);
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
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o email: " + email));
    }

    @Transactional(readOnly = true)
    public List<UserDetailsDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDetailsDTO updateUser(Long userId, UserRegistrationDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + userId));

        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());

        // Buscar a empresa pelo nome (se fornecido), usando orElseThrow
        if(userDTO.getEmpresaNome() != null){
            Empresa empresa = empresaRepository.findByNome(userDTO.getEmpresaNome())
                    .orElseThrow(() -> new RuntimeException("Empresa não encontrada: "+ userDTO.getEmpresaNome()));
            user.setEmpresa(empresa);
        }


        user = userRepository.save(user);
        return new UserDetailsDTO(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + userId));

        userRepository.delete(user);
    }
}