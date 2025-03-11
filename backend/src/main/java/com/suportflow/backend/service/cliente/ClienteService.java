package com.suportflow.backend.service.cliente;

import com.suportflow.backend.dto.ClienteDTO;
import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.dto.ClienteUpdateDTO;
import com.suportflow.backend.dto.PasswordChangeDTO;
import com.suportflow.backend.exception.UniqueFieldAlreadyExistsException;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.EmpresaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

  @Autowired private ClienteRepository clienteRepository;
  @Autowired private EmpresaRepository empresaRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public ClienteDTO findDTOById(Long id) {
    return clienteRepository
        .findById(id)
        .map(ClienteDTO::new)
        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o ID: " + id));
  }

  @Transactional(readOnly = true)
  public ClienteDTO findDTOByEmail(String email) {
    return clienteRepository
        .findByEmail(email)
        .map(ClienteDTO::new)
        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o email: " + email));
  }

  @Transactional(readOnly = true)
  public Cliente findEntityById(Long id) {
    return clienteRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o ID: " + id));
  }

  @Transactional(readOnly = true)
  public List<ClienteDTO> findAllDTO() {
    return clienteRepository.findAll().stream().map(ClienteDTO::new).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<Cliente> findAll() { // Consider removing, as findAllDTO is usually preferred.
    return clienteRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Cliente findByEmail(String email) { // Keep this for internal use.
    return clienteRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o email: " + email));
  }

  @Transactional
  public ClienteDTO save(ClienteRegistrationDTO clienteRegistrationDTO) {
    // Duplicate checks
    if (clienteRepository.existsByEmail(clienteRegistrationDTO.getEmail())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
    }

    if (clienteRegistrationDTO.getCpfCnpj() != null
        && clienteRepository.existsByCpfCnpj(clienteRegistrationDTO.getCpfCnpj())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
    }

    if (clienteRegistrationDTO.getTelefone() != null
        && clienteRepository.existsByTelefone(clienteRegistrationDTO.getTelefone())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
    }

    Empresa empresa = null;
    if (clienteRegistrationDTO.getEmpresaNome() != null) {
      empresa =
          empresaRepository
              .findByNome(clienteRegistrationDTO.getEmpresaNome())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Empresa não encontrada: " + clienteRegistrationDTO.getEmpresaNome()));
    }

    Cliente cliente = new Cliente();
    cliente.setNome(clienteRegistrationDTO.getNome());
    cliente.setEmail(clienteRegistrationDTO.getEmail());
    cliente.setTelefone(clienteRegistrationDTO.getTelefone());
    cliente.setCpfCnpj(clienteRegistrationDTO.getCpfCnpj());
    cliente.setEmpresa(empresa);
    cliente.setSenha(passwordEncoder.encode(clienteRegistrationDTO.getSenha()));
    cliente.setDataCadastro(LocalDateTime.now());
    cliente.setAtivo(true);

    Cliente savedCliente = clienteRepository.save(cliente);
    return new ClienteDTO(savedCliente);
  }

  @Transactional
  public ClienteDTO update(Long id, ClienteUpdateDTO clienteAtualizado) {
    Cliente clienteExistente = findEntityById(id); // Use consistent findEntityById

    // Unique field checks (only if changed)
    if (clienteAtualizado.getEmail() != null
        && !clienteExistente.getEmail().equals(clienteAtualizado.getEmail())) {
      if (clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
        throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
      }
      clienteExistente.setEmail(clienteAtualizado.getEmail());
    }

    if (clienteAtualizado.getCpfCnpj() != null) {
      if (!Objects.equals(clienteExistente.getCpfCnpj(), clienteAtualizado.getCpfCnpj())) {
        if (clienteAtualizado.getCpfCnpj() != null
            && clienteRepository.existsByCpfCnpj(clienteAtualizado.getCpfCnpj())) {
          throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
        }
        clienteExistente.setCpfCnpj(clienteAtualizado.getCpfCnpj());
      }
    }

    if (clienteAtualizado.getTelefone() != null) {
      if (!Objects.equals(clienteExistente.getTelefone(), clienteAtualizado.getTelefone())) {
        if (clienteAtualizado.getTelefone() != null
            && clienteRepository.existsByTelefone(clienteAtualizado.getTelefone())) {
          throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
        }
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
      }
    }

    // Other field updates
    if (clienteAtualizado.getNome() != null) {
      clienteExistente.setNome(clienteAtualizado.getNome());
    }

    if (clienteAtualizado.getEmpresaNome() != null) {
      Empresa empresa =
          empresaRepository
              .findByNome(clienteAtualizado.getEmpresaNome())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Empresa não encontrada: " + clienteAtualizado.getEmpresaNome()));
      clienteExistente.setEmpresa(empresa);
    }

    Cliente updatedCliente = clienteRepository.save(clienteExistente);
    return new ClienteDTO(updatedCliente);
  }

  @Transactional
  public ClienteDTO updateByEmail(String email, ClienteUpdateDTO clienteUpdateDTO) {
    Cliente clienteExistente = findByEmail(email); // Find by email!

    // Unique field checks (only if changed), similar to update(Long id, ...)
    if (clienteUpdateDTO.getEmail() != null
        && !clienteExistente.getEmail().equals(clienteUpdateDTO.getEmail())) {
      if (clienteRepository.existsByEmail(clienteUpdateDTO.getEmail())) {
        throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
      }
      clienteExistente.setEmail(clienteUpdateDTO.getEmail());
    }

    if (clienteUpdateDTO.getCpfCnpj() != null) {
      if (!Objects.equals(clienteExistente.getCpfCnpj(), clienteUpdateDTO.getCpfCnpj())) {
        if (clienteUpdateDTO.getCpfCnpj() != null
            && clienteRepository.existsByCpfCnpj(clienteUpdateDTO.getCpfCnpj())) {
          throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
        }
        clienteExistente.setCpfCnpj(clienteUpdateDTO.getCpfCnpj());
      }
    }

    if (clienteUpdateDTO.getTelefone() != null) {
      if (!Objects.equals(clienteExistente.getTelefone(), clienteUpdateDTO.getTelefone())) {
        if (clienteUpdateDTO.getTelefone() != null
            && clienteRepository.existsByTelefone(clienteUpdateDTO.getTelefone())) {
          throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
        }
        clienteExistente.setTelefone(clienteUpdateDTO.getTelefone());
      }
    }

    // Other field updates (similar to update method)
    if (clienteUpdateDTO.getNome() != null) {
      clienteExistente.setNome(clienteUpdateDTO.getNome());
    }
    if (clienteUpdateDTO.getEmpresaNome() != null) {
      Empresa empresa =
          empresaRepository
              .findByNome(clienteUpdateDTO.getEmpresaNome())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Empresa não encontrada: " + clienteUpdateDTO.getEmpresaNome()));
      clienteExistente.setEmpresa(empresa);
    }

    Cliente updatedCliente = clienteRepository.save(clienteExistente);
    return new ClienteDTO(updatedCliente);
  }

  @Transactional
  public void changePasswordByEmail(String email, PasswordChangeDTO passwordChangeDTO) {
    Cliente cliente = findByEmail(email); // Find by email!

    if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), cliente.getSenha())) {
      throw new IllegalArgumentException("Senha antiga incorreta.");
    }
    if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
      throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
    }

    cliente.setSenha(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
    clienteRepository.save(cliente);
  }

  @Transactional
  public void changePassword(Long id, PasswordChangeDTO passwordChangeDTO) {
    Cliente cliente = findEntityById(id); // Use findEntityById

    // Verify old password
    if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), cliente.getSenha())) {
      throw new IllegalArgumentException("Senha antiga incorreta.");
    }

    // Check if new password and confirmation match
    if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
      throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
    }

    // Encode and set the new password
    String encodedNewPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
    cliente.setSenha(encodedNewPassword);
    clienteRepository.save(cliente);
  }

  @Transactional
  public void delete(Long id) {
    clienteRepository.delete(findEntityById(id)); // Use consistent findEntityById
  }
}