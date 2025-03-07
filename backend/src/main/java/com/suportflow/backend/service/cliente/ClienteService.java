package com.suportflow.backend.service.cliente;

import com.suportflow.backend.dto.ClienteRegistrationDTO;
import com.suportflow.backend.exception.UserNotFoundException;
import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository; // Para associar clientes a empresas

    @Autowired
    private PasswordEncoder passwordEncoder; // Importante para criptografar a "senha"

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
        // Verifica se o email já existe
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new DataIntegrityViolationException("Já existe um cliente com este email.");
        }

        // Verifica se o CPF/CNPJ já existe
        if (clienteRepository.existsByCpfCnpj(cliente.getCpfCnpj())) {
            throw new DataIntegrityViolationException("Já existe um cliente com este CPF/CNPJ.");
        }

        // Criptografa a senha (que é o CPF/CNPJ) antes de salvar
        cliente.setSenha(passwordEncoder.encode(cliente.getCpfCnpj()));
        cliente.setDataCadastro(LocalDateTime.now()); // Define a data de cadastro
        return clienteRepository.save(cliente);
    }

    @Transactional // Sem readOnly, pois é uma operação de escrita
    public Cliente update(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = findById(id); // Reutiliza o findById para tratar cliente não encontrado

        // Verifica se o novo email já existe (se o email foi alterado)
        if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
            throw new DataIntegrityViolationException("Já existe um cliente com este email.");
        }

        // Verifica se o novo CPF/CNPJ já existe (se foi alterado)
        if (!clienteExistente.getCpfCnpj().equals(clienteAtualizado.getCpfCnpj()) &&
                clienteRepository.existsByCpfCnpj(clienteAtualizado.getCpfCnpj())) {
            throw new DataIntegrityViolationException("Já existe um cliente com este CPF/CNPJ.");
        }
        // Atualiza os campos.  Não atualizamos a senha aqui!
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setCpfCnpj(clienteAtualizado.getCpfCnpj());

        // Associação com Empresa (se fornecida)
        if (clienteAtualizado.getEmpresa() != null && clienteAtualizado.getEmpresa().getId() != null) {
            Empresa empresa = empresaRepository.findById(clienteAtualizado.getEmpresa().getId())
                    .orElseThrow(() -> new RuntimeException("Empresa com ID " + clienteAtualizado.getEmpresa().getId() + " não encontrada."));
            clienteExistente.setEmpresa(empresa);
        }

        return clienteRepository.save(clienteExistente); // Salva as alterações
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = findById(id); // Verifica se existe e lança exceção se não existir
        clienteRepository.delete(cliente);
    }
}