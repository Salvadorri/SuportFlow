// src/main/java/com/suportflow/backend/service/cliente/ClienteService.java
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
  public ClienteDTO findById(Long id) {
    Cliente cliente =
        clienteRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o ID: " + id));
    return new ClienteDTO(cliente); // Convert to DTO
  }

  @Transactional(readOnly = true)
  public Cliente findEntityById(Long id) { // Added for internal use (security checks)
    return clienteRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o ID: " + id));
  }

  @Transactional(readOnly = true)
  public List<ClienteDTO> findAllDTO() {
    return clienteRepository.findAll().stream().map(ClienteDTO::new).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<Cliente> findAll() {
    return clienteRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Cliente findByEmail(String email) {
    return clienteRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o email: " + email));
  }

  @Transactional
  public ClienteDTO save(ClienteRegistrationDTO clienteRegistrationDTO) {
    if (clienteRepository.existsByEmail(clienteRegistrationDTO.getEmail())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
    }

    if (clienteRepository.existsByCpfCnpj(clienteRegistrationDTO.getCpfCnpj())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
    }

    if (clienteRepository.existsByTelefone(clienteRegistrationDTO.getTelefone())) {
      throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
    }

    // Find Empresa, if provided
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
    cliente.setEmpresa(empresa); // Set the Empresa
    cliente.setSenha(passwordEncoder.encode(clienteRegistrationDTO.getSenha()));
    cliente.setDataCadastro(LocalDateTime.now());
    cliente.setAtivo(true); // Ensure new clients are active

    Cliente savedCliente = clienteRepository.save(cliente);
    return new ClienteDTO(savedCliente); // Return the DTO
  }

  @Transactional
  public ClienteDTO update(Long id, ClienteUpdateDTO clienteAtualizado) {
    Cliente clienteExistente = findEntityById(id); // Use the entity version

    // Check if email is being changed and if the new email already exists
    if (clienteAtualizado.getEmail() != null
        && !clienteExistente.getEmail().equals(clienteAtualizado.getEmail())) {
      if (clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
        throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
      }
      clienteExistente.setEmail(clienteAtualizado.getEmail());
    }

    // Check if CPF/CNPJ is being changed and if the new CPF/CNPJ already exists
    if (clienteAtualizado.getCpfCnpj() != null
        && !clienteExistente.getCpfCnpj().equals(clienteAtualizado.getCpfCnpj())) {
      if (clienteRepository.existsByCpfCnpj(clienteAtualizado.getCpfCnpj())) {
        throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
      }
      clienteExistente.setCpfCnpj(clienteAtualizado.getCpfCnpj());
    }

    // Check if telephone is being changed
    if (clienteAtualizado.getTelefone() != null
        && !clienteExistente.getTelefone().equals(clienteAtualizado.getTelefone())) {
      if (clienteRepository.existsByTelefone(clienteAtualizado.getTelefone())) {
        throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
      }
      clienteExistente.setTelefone(clienteAtualizado.getTelefone());
    }

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
    Cliente cliente = findEntityById(id); // Use entity version
    clienteRepository.delete(cliente);
  }
}