package com.suportflow.backend.service.auth;

import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        // Criptografa a senha antes de salvar no banco de dados
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Define a data de criação
        user.setDataCriacao(LocalDateTime.now());

        // Define o usuário como ativo (ou false, dependendo da sua lógica)
        user.setAtivo(true);

        // Salva o usuário no banco de dados
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Usuário não encontrado com o email: " + email);
        }
        return user;
    }

    public List<User> findAllUsers() {
        // Busca todos os usuários
        return userRepository.findAll();
    }

    // Adicione outros métodos conforme necessário, por exemplo:

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user) {
        // Atualiza um usuário existente
        // Verifique se o usuário existe antes de tentar atualizar
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        // Exclui um usuário pelo ID
        userRepository.deleteById(id);
    }

    // ...
}