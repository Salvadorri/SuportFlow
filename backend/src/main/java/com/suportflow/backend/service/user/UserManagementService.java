//backend/src/main/java/com/suportflow/backend/service/user/UserManagementService.java
package com.suportflow.backend.service.user;

import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.dto.UserDetailsDTO;
import com.suportflow.backend.dto.UserRegistrationDTO;
import com.suportflow.backend.dto.UserUpdateDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Chamado;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.Permissao;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ChamadoRepository;
import com.suportflow.backend.repository.EmpresaRepository;
import com.suportflow.backend.repository.PermissaoRepository;
import com.suportflow.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.suportflow.backend.exception.UniqueFieldAlreadyExistsException;
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

  @Autowired private PermissaoRepository permissaoRepository; // Injete o PermissaoRepository

  @Autowired private ChamadoRepository chamadoRepository; // Inject ChamadoRepository

  @Transactional
  public UserDetailsDTO registerNewUser(UserRegistrationDTO registrationDTO) {
    // Existing email check
    if (userRepository.existsByEmail(registrationDTO.getEmail())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este e-mail.");
    }

    // CPF/CNPJ check.  Important:  Handle nulls!
    if (registrationDTO.getCpfCnpj() != null
            && userRepository.existsByCpfCnpj(registrationDTO.getCpfCnpj())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este CPF/CNPJ.");
    }

    String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

    Empresa empresa = null;
    if (registrationDTO.getEmpresaNome() != null) {
      empresa =
              empresaRepository
                      .findByNome(registrationDTO.getEmpresaNome())
                      .orElseThrow(
                              () ->
                                      new RuntimeException( //Use a more generic exception here.
                                              "Empresa não encontrada: " + registrationDTO.getEmpresaNome()));
    }

    User user = new User();
    user.setNome(registrationDTO.getNome());
    user.setEmail(registrationDTO.getEmail());
    user.setSenha(encodedPassword); // Use the encoded password
    user.setEmpresa(empresa);
    user.setAtivo(true);
    user.setDataCriacao(LocalDateTime.now());
    // NEW: Set phone and cpfCnpj
    user.setTelefone(registrationDTO.getTelefone());
    user.setCpfCnpj(registrationDTO.getCpfCnpj());

    // --- Permissões ---
    Set<Permissao> permissoes = new HashSet<>();
    if (registrationDTO.getRoles() != null) { // Importante: Verifique se a lista não é nula
      for (String roleName : registrationDTO.getRoles()) {
        Permissao permissao =
                permissaoRepository
                        .findByNome(roleName)
                        .orElseThrow(
                                () ->
                                        new RuntimeException( //Use a more generic exception here
                                                "Permissão não encontrada: "
                                                        + roleName)); // Melhor tratamento de exceção
        permissoes.add(permissao);
      }
    }
    user.setPermissoes(permissoes); // Associa as permissões ao usuário

    try {
      user = userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      // This catch block is likely redundant now, handled by GlobalExceptionHandler,
      // but kept for extra safety.  You could log the specific cause here.
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este e-mail ou CPF/CNPJ.", e);
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
  public UserDetailsDTO updateUserByEmail(String email, UserUpdateDTO userDTO) { // Use UserUpdateDTO
    User user = findByEmail(email); // Use findByEmail for consistency

    // Check for email conflicts *only if* the email is being changed.
    if (userDTO.getEmail() != null
            && !user.getEmail().equals(userDTO.getEmail())
            && userRepository.existsByEmail(userDTO.getEmail())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este e-mail.");
    }

    // Check for CPF/CNPJ conflicts, *only if* the CPF/CNPJ is being changed.
    if (userDTO.getCpfCnpj() != null
            && !userDTO.getCpfCnpj().equals(user.getCpfCnpj())
            && userRepository.existsByCpfCnpj(userDTO.getCpfCnpj())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este CPF/CNPJ.");
    }
    // Only update if the value is present in the DTO
    if (userDTO.getNome() != null) {
      user.setNome(userDTO.getNome());
    }
    if (userDTO.getEmail() != null) {
      user.setEmail(userDTO.getEmail());
    }
    if (userDTO.getTelefone() != null) {
      user.setTelefone(userDTO.getTelefone());
    }
    if (userDTO.getCpfCnpj() != null) {
      user.setCpfCnpj(userDTO.getCpfCnpj());
    }
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
  public UserDetailsDTO updateUser(Long id, UserUpdateDTO userDTO) { // Use UserUpdateDTO
    User user =
            userRepository
                    .findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + id));

    // Check for email conflicts *only if* the email is being changed.
    if (userDTO.getEmail() != null
            && !user.getEmail().equals(userDTO.getEmail())
            && userRepository.existsByEmail(userDTO.getEmail())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este e-mail.");
    }

    // Check for CPF/CNPJ conflicts, *only if* the CPF/CNPJ is being changed.
    if (userDTO.getCpfCnpj() != null
            && !userDTO.getCpfCnpj().equals(user.getCpfCnpj())
            && userRepository.existsByCpfCnpj(userDTO.getCpfCnpj())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um usuário com este CPF/CNPJ.");
    }

    // Only update if the value is present in the DTO
    if (userDTO.getNome() != null) {
      user.setNome(userDTO.getNome());
    }
    if (userDTO.getEmail() != null) {
      user.setEmail(userDTO.getEmail());
    }
    if (userDTO.getTelefone() != null) {
      user.setTelefone(userDTO.getTelefone());
    }
    if (userDTO.getCpfCnpj() != null) {
      user.setCpfCnpj(userDTO.getCpfCnpj());
    }

    if (userDTO.getEmpresaNome() != null) {
      Empresa empresa =
              empresaRepository
                      .findByNome(userDTO.getEmpresaNome())
                      .orElseThrow(
                              () -> new RuntimeException("Empresa não encontrada: " + userDTO.getEmpresaNome()));
      user.setEmpresa(empresa);
    }

    user = userRepository.save(user);
    return new UserDetailsDTO(user);
  }

  @Transactional
  public void changePassword(String email, PasswordChangeDTO passwordChangeDTO) {
    User user = findByEmail(email);

    // Verify old password
    if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getSenha())) {
      throw new IllegalArgumentException("Senha antiga incorreta."); // This is fine, GlobalExceptionHandler will catch it
    }

    // Check if new password and confirmation match
    if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
      throw new IllegalArgumentException("A nova senha e a confirmação não coincidem."); // This is fine
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

    // No need to nullify refresh tokens, ON DELETE CASCADE will handle it

    // Nullify the user_id in chamados table
    List<Chamado> chamados = chamadoRepository.findByAtendente(user);
    for (Chamado chamado : chamados) {
      chamado.setAtendente(null); // Set the atendente to null
      chamadoRepository.save(chamado);
    }

    userRepository.delete(user);
  }
}