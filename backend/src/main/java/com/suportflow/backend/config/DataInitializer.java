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
        Permissao superAdminPermissao = criarPermissaoSeNaoExistir("SUPER_ADMIN", "Permissão de Super Administrador");
        Permissao adminPermissao = criarPermissaoSeNaoExistir("ADMIN", "Permissão de Administrador");
        Permissao gerenciarEmpresasPermissao = criarPermissaoSeNaoExistir("GERENCIAR_EMPRESAS", "Gerenciar contas de empresas");
        Permissao darSuporteAoSistemaPermissao = criarPermissaoSeNaoExistir("DAR_SUPORTE_SISTEMA", "Fornecer suporte ao sistema");
        Permissao atualizarChatbotEmpresasPermissao = criarPermissaoSeNaoExistir("ATUALIZAR_CHATBOT_EMPRESAS", "Atualizar e otimizar o chatbot IA de outras empresas");
        Permissao visualizarRelatoriosPermissao = criarPermissaoSeNaoExistir("VISUALIZAR_RELATORIOS", "Visualizar relatórios");
        Permissao criarUsuarioPermissao = criarPermissaoSeNaoExistir("CRIAR_USUARIO", "Permissão para Criar Usuários");
        Permissao atualizarUsuarioPermissao = criarPermissaoSeNaoExistir("ATUALIZAR_USUARIO", "Permissão para Modificar Usuários");
        Permissao excluirUsuarioPermissao = criarPermissaoSeNaoExistir("EXCLUIR_USUARIO", "Permissão para Excluir Usuários");
        Permissao gerenciarPermissoesEmpresasPermissao = criarPermissaoSeNaoExistir("GERENCIAR_PERMISSOES_EMPRESAS", "Permissão para gerenciar permissões de usuários de outras empresas");
        Permissao gerenciarEquipePermissao = criarPermissaoSeNaoExistir("GERENCIAR_EQUIPE", "Gerenciar contas de funcionários da própria empresa");
        Permissao criarFuncionarioPermissao = criarPermissaoSeNaoExistir("CRIAR_FUNCIONARIO", "Criar contas de funcionários");
        Permissao atualizarFuncionarioPermissao = criarPermissaoSeNaoExistir("ATUALIZAR_FUNCIONARIO", "Editar contas de funcionários");
        Permissao excluirFuncionarioPermissao = criarPermissaoSeNaoExistir("EXCLUIR_FUNCIONARIO", "Excluir contas de funcionários");
        Permissao visualizarRelatoriosEquipePermissao = criarPermissaoSeNaoExistir("VISUALIZAR_RELATORIOS_EQUIPE", "Acessar relatórios detalhados sobre o desempenho da equipe");
        Permissao visualizarFeedbackClientesPermissao = criarPermissaoSeNaoExistir("VISUALIZAR_FEEDBACK_CLIENTES", "Visualizar avaliações e feedbacks de clientes da própria empresa");
        Permissao definirPrioridadesPermissao = criarPermissaoSeNaoExistir("DEFINIR_PRIORIDADES", "Configurar prioridades para a gestão de chamados");
        Permissao abrirChamadoSuportePermissao = criarPermissaoSeNaoExistir("ABRIR_CHAMADO_SUPORTE", "Abrir chamados para a equipe de administração do sistema");
        Permissao atualizarChatbotPermissao = criarPermissaoSeNaoExistir("ATUALIZAR_CHATBOT", "Atualizar e otimizar o chatbot IA");
        Permissao gerenciarPermissoesPermissao = criarPermissaoSeNaoExistir("GERENCIAR_PERMISSOES", "Permissão para gerenciar permissões de usuários");
        Permissao gerenciarChamadosPermissao = criarPermissaoSeNaoExistir("GERENCIAR_CHAMADOS", "Gerenciar chamados de clientes");
        Permissao atribuirChamadosPermissao = criarPermissaoSeNaoExistir("ATRIBUIR_CHAMADOS", "Atribuir chamados");
        Permissao visualizarChamadosPermissao = criarPermissaoSeNaoExistir("VISUALIZAR_CHAMADOS", "Visualizar chamados");
        Permissao responderChamadosPermissao = criarPermissaoSeNaoExistir("RESPONDER_CHAMADOS", "Responder chamados");
        Permissao fecharChamadosPermissao = criarPermissaoSeNaoExistir("FECHAR_CHAMADOS", "Fechar chamados (resolver)");
        Permissao visualizarHistoricoChamadosPermissao = criarPermissaoSeNaoExistir("VISUALIZAR_HISTORICO_CHAMADOS", "Acessar o histórico de interações com clientes");
        Permissao acessarBaseConhecimentoPermissao = criarPermissaoSeNaoExistir("ACESSAR_BASE_CONHECIMENTO", "Consultar a base de conhecimento");
        Permissao classificarChamadosPermissao = criarPermissaoSeNaoExistir("CLASSIFICAR_CHAMADOS", "Organizar chamados por prioridade ou tipo de problema");
        Permissao usarChatbotPermissao = criarPermissaoSeNaoExistir("USAR_CHATBOT", "Usar o chatbot IA");
        Permissao abrirChamadoPermissao = criarPermissaoSeNaoExistir("ABRIR_CHAMADO", "Abrir um chamado");


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
            permissoes.add(superAdminPermissao);
            permissoes.add(adminPermissao);
            permissoes.add(gerenciarEmpresasPermissao);
            permissoes.add(darSuporteAoSistemaPermissao);
            permissoes.add(atualizarChatbotEmpresasPermissao);
            permissoes.add(visualizarRelatoriosPermissao);
            permissoes.add(criarUsuarioPermissao);
            permissoes.add(atualizarUsuarioPermissao);
            permissoes.add(excluirUsuarioPermissao);
            permissoes.add(gerenciarPermissoesEmpresasPermissao);
            permissoes.add(gerenciarEquipePermissao);
            permissoes.add(criarFuncionarioPermissao);
            permissoes.add(atualizarFuncionarioPermissao);
            permissoes.add(excluirFuncionarioPermissao);
            permissoes.add(visualizarRelatoriosEquipePermissao);
            permissoes.add(visualizarFeedbackClientesPermissao);
            permissoes.add(definirPrioridadesPermissao);
            permissoes.add(abrirChamadoSuportePermissao);
            permissoes.add(atualizarChatbotPermissao);
            permissoes.add(gerenciarPermissoesPermissao);
            permissoes.add(gerenciarChamadosPermissao);
            permissoes.add(atribuirChamadosPermissao);
            permissoes.add(visualizarChamadosPermissao);
            permissoes.add(responderChamadosPermissao);
            permissoes.add(fecharChamadosPermissao);
            permissoes.add(visualizarHistoricoChamadosPermissao);
            permissoes.add(acessarBaseConhecimentoPermissao);
            permissoes.add(classificarChamadosPermissao);
            permissoes.add(usarChatbotPermissao);
            permissoes.add(abrirChamadoPermissao);

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