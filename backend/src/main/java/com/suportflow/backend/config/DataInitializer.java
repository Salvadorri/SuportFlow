package com.suportflow.backend.config;

import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.Permissao;
import com.suportflow.backend.model.User;
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
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Cria a empresa SuportFlow SE NÃO EXISTIR, usando orElseGet
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

        // Cria as permissões (usando o método auxiliar)
        Permissao adminPermissao = criarPermissaoSeNaoExistir("ADMIN", "Permissão de Administrador");
        Permissao gerentePermissao = criarPermissaoSeNaoExistir("GERENTE", "Permissão de Gerente");
        Permissao atendentePermissao = criarPermissaoSeNaoExistir("ATENDENTE", "Permissão de Atendente");

        // Cria o usuário Admin se não existir, usando orElseGet
        User adminUser = userRepository.findByEmail(adminEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setNome("Administrador do Sistema");
            newUser.setEmail(adminEmail);
            newUser.setSenha(passwordEncoder.encode(adminPassword));
            newUser.setEmpresa(suportFlowEmpresa);
            newUser.setAtivo(true);
            newUser.setDataCriacao(LocalDateTime.now());

            // Adiciona TODAS as permissões criadas ao conjunto.
            Set<Permissao> permissoes = new HashSet<>();
            permissoes.add(adminPermissao);

            newUser.setPermissoes(permissoes); // Associa as permissões
            return userRepository.save(newUser); // Salva o novo usuário

        });
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