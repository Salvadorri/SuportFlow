// src/main/java/com/suportflow/backend/service/cliente/ClienteService.java
package com.suportflow.backend.service.cliente;

import com.suportflow.backend.exception.UniqueFieldAlreadyExistsException;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente findByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cliente não encontrado com o email: " + email));
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
        }

        if (clienteRepository.existsByCpfCnpj(cliente.getCpfCnpj())) {
            throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
        }

        if (clienteRepository.existsByTelefone(cliente.getTelefone())) {
            throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setDataCadastro(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = findById(id);

        // Check if email is being changed and if the new email already exists
        if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail())) {
            if (clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
                throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este email.");
            }
        }

        // Check if CPF/CNPJ is being changed and if the new CPF/CNPJ already exists
        if (!clienteExistente.getCpfCnpj().equals(clienteAtualizado.getCpfCnpj())) {
            if (clienteRepository.existsByCpfCnpj(clienteAtualizado.getCpfCnpj())) {
                throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este CPF/CNPJ.");
            }
        }

        // Check if telephone is being changed and if the new telephone already exists
        if (!clienteExistente.getTelefone().equals(clienteAtualizado.getTelefone())) {
            if (clienteRepository.existsByTelefone(clienteAtualizado.getTelefone())) {
                throw new UniqueFieldAlreadyExistsException("Já existe um cliente com este telefone.");
            }
        }

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setCpfCnpj(clienteAtualizado.getCpfCnpj());

        if (clienteAtualizado.getEmpresa() != null && clienteAtualizado.getEmpresa().getId() != null) {
            Empresa empresa = empresaRepository.findById(clienteAtualizado.getEmpresa().getId())
                    .orElseThrow(() -> new RuntimeException("Empresa com ID " + clienteAtualizado.getEmpresa().getId() + " não encontrada."));
            clienteExistente.setEmpresa(empresa);
        }

        return clienteRepository.save(clienteExistente);
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = findById(id);
        clienteRepository.delete(cliente);
    }
}