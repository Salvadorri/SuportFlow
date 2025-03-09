// src/main/java/com/suportflow/backend/config/DataInitializer.java
package com.suportflow.backend.config;

import com.suportflow.backend.model.Cliente;
import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.Permissao;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.ClienteRepository;
import com.suportflow.backend.repository.EmpresaRepository;
import com.suportflow.backend.repository.PermissaoRepository;
import com.suportflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String DEFAULT_CLIENT_PASSWORD = "SenhaPadrao123!";

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // Criar/Encontrar Empresa SuportFlow
        Empresa suportFlowEmpresa = empresaRepository.findByNome("SuportFlow").orElseGet(() -> {
            Empresa newEmpresa = new Empresa();
            newEmpresa.setNome("SuportFlow");
            newEmpresa.setEmail("contato@suportflow.com");
            newEmpresa.setEndereco("Rua Central, 123");
            newEmpresa.setTelefone("(00) 1234-5678");
            newEmpresa.setCnpj("12345678901234");
            newEmpresa.setDataCriacao(LocalDateTime.now());
            return empresaRepository.save(newEmpresa);
        });

        // Criar/Encontrar Permissões
        Permissao adminPermissao = criarPermissaoSeNaoExistir("ROLE_ADMIN", "Permissão de Administrador");
        Permissao gerentePermissao = criarPermissaoSeNaoExistir("ROLE_GERENTE", "Permissão de Gerente");
        Permissao atendentePermissao = criarPermissaoSeNaoExistir("ROLE_ATENDENTE", "Permissão de Atendente");

        // Criar/Encontrar Usuário Administrador
        User adminUser = userRepository.findByEmail(adminEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setNome("Administrador do Sistema");
            newUser.setEmail(adminEmail);
            newUser.setSenha(passwordEncoder.encode(adminPassword));
            newUser.setEmpresa(suportFlowEmpresa);
            newUser.setAtivo(true);
            newUser.setDataCriacao(LocalDateTime.now());
            Set<Permissao> permissoes = new HashSet<>();
            permissoes.add(adminPermissao);
            newUser.setPermissoes(permissoes);
            return userRepository.save(newUser);
        });

        // Criar Clientes de Exemplo
        criarClienteSeNaoExistir("cliente1@example.com", "12345678901", "Cliente 1", suportFlowEmpresa); // CPF
        criarClienteSeNaoExistir("cliente2@example.com", "12345678901234", "Cliente 2", suportFlowEmpresa); // CNPJ
    }

    private void criarClienteSeNaoExistir(String email, String cpfCnpj, String nome, Empresa empresa) {
        if (!clienteRepository.existsByEmail(email) && !clienteRepository.existsByCpfCnpj(cpfCnpj)) {
            Cliente cliente = new Cliente();
            cliente.setEmail(email);
            cliente.setCpfCnpj(cpfCnpj);
            cliente.setSenha(passwordEncoder.encode(DEFAULT_CLIENT_PASSWORD)); // Senha padrão criptografada
            cliente.setNome(nome);
            cliente.setEmpresa(empresa);
            cliente.setDataCadastro(LocalDateTime.now());
            clienteRepository.save(cliente);
        }
    }

    private Permissao criarPermissaoSeNaoExistir(String nome, String descricao) {
        return permissaoRepository.findByNome(nome)
                .orElseGet(() -> {
                    Permissao newPermissao = new Permissao();
                    newPermissao.setNome(nome);
                    newPermissao.setDescricao(descricao);
                    return permissaoRepository.save(newPermissao);
                });
    }
}