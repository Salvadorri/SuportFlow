package com.suportflow.backend.config;

import com.suportflow.backend.model.Empresa;
import com.suportflow.backend.model.Permissao;
import com.suportflow.backend.model.User;
import com.suportflow.backend.repository.EmpresaRepository;
import com.suportflow.backend.repository.PermissaoRepository;
import com.suportflow.backend.repository.UserRepository;
import com.suportflow.backend.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService; // Adicionando o UserService

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Cria a empresa SuportFlow se não existir
        Empresa empresa = empresaRepository.findByNome("SuportFlow");
        if (empresa == null) {
            empresa = new Empresa();
            empresa.setNome("SuportFlow");
            empresa.setEmail("contato@suportflow.com");
            empresa.setEndereco("Rua Central, 123");
            empresa.setTelefone("(00) 1234-5678");
            empresa.setCnpj("12345678901234");
            empresa.setDataCriacao(LocalDateTime.now());
            empresa = empresaRepository.save(empresa);
        }

        // Cria as permissões se não existirem
        Permissao adminPermissao = criarPermissaoSeNaoExistir("ADMIN", "Permissão de Administrador");
        Permissao verTodosChamadosPermissao = criarPermissaoSeNaoExistir("VER_TODOS_CHAMADOS", "Permissão para Ver todos os chamados");
        Permissao criarChamadosPermissao = criarPermissaoSeNaoExistir("CRIAR_CHAMADOS", "Permissão para Criar chamados");
        Permissao responderChamadosPermissao = criarPermissaoSeNaoExistir("RESPONDER_CHAMADOS", "Permissão para Responder chamados");
        Permissao fecharChamadosPermissao = criarPermissaoSeNaoExistir("FECHAR_CHAMADOS", "Permissão para Fechar chamados");
        Permissao atribuirChamadosPermissao = criarPermissaoSeNaoExistir("ATRIBUIR_CHAMADOS", "Permissão para Atribuir chamados");
        Permissao gerenciarBaseConhecimentoPermissao = criarPermissaoSeNaoExistir("GERENCIAR_BASE_CONHECIMENTO", "Permissão para Gerenciar Base de Conhecimento");

        // Cria a permissão de SUPER ADMIN
        Permissao superAdminPermissao = criarPermissaoSeNaoExistir("SUPER_ADMIN", "Permissão de Super Administrador");

        // Cria o usuário Admin se não existir e associa as permissões, incluindo SUPER_ADMIN
        User adminUser = userRepository.findByEmail("admin@suportflow.com");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setNome("Administrador");
            adminUser.setEmail("admin@suportflow.com");
            adminUser.setSenha(passwordEncoder.encode("admin123"));
            adminUser.setEmpresa(empresa);

            Set<Permissao> permissoes = new HashSet<>();
            permissoes.add(adminPermissao);
            permissoes.add(verTodosChamadosPermissao);
            permissoes.add(criarChamadosPermissao);
            permissoes.add(responderChamadosPermissao);
            permissoes.add(fecharChamadosPermissao);
            permissoes.add(atribuirChamadosPermissao);
            permissoes.add(gerenciarBaseConhecimentoPermissao);
            permissoes.add(superAdminPermissao);
            adminUser.setPermissoes(permissoes);

            adminUser.setAtivo(true);
            adminUser.setDataCriacao(LocalDateTime.now());
            userRepository.save(adminUser);
        } else {
            // Usa o UserService para adicionar a permissão SUPER_ADMIN se necessário
            userService.adicionarPermissaoSuperAdminSeNecessario(adminUser.getEmail(), superAdminPermissao);
        }
    }

    private Permissao criarPermissaoSeNaoExistir(String nome, String descricao) {
        Permissao permissao = permissaoRepository.findByNome(nome);
        if (permissao == null) {
            permissao = new Permissao();
            permissao.setNome(nome);
            permissao.setDescricao(descricao);
            permissao = permissaoRepository.save(permissao);
        }
        return permissao;
    }
}