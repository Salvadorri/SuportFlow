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
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Cria a empresa SuportFlow se não existir
        Empresa suportFlowEmpresa = empresaRepository.findByNome("SuportFlow");
        if (suportFlowEmpresa == null) {
            suportFlowEmpresa = new Empresa();
            suportFlowEmpresa.setNome("SuportFlow");
            suportFlowEmpresa.setEmail("contato@suportflow.com");
            suportFlowEmpresa.setEndereco("Rua Central, 123");
            suportFlowEmpresa.setTelefone("(00) 1234-5678");
            suportFlowEmpresa.setCnpj("12345678901234");
            suportFlowEmpresa.setDataCriacao(LocalDateTime.now());
            suportFlowEmpresa = empresaRepository.save(suportFlowEmpresa);
        }

        // **********************************************
        // ******* Permissões de Administrador **********
        // **********************************************
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

        // **********************************************
        // ****** Permissões de Gerentes de Empresas *****
        // **********************************************
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

        // **********************************************
        // ****** Permissões de Funcionários de Empresas **
        // **********************************************
        Permissao gerenciarChamadosPermissao = criarPermissaoSeNaoExistir("GERENCIAR_CHAMADOS", "Gerenciar chamados de clientes");
        Permissao atribuirChamadosPermissao = criarPermissaoSeNaoExistir("ATRIBUIR_CHAMADOS", "Atribuir chamados");
        Permissao visualizarChamadosPermissao = criarPermissaoSeNaoExistir("VISUALIZAR_CHAMADOS", "Visualizar chamados");
        Permissao responderChamadosPermissao = criarPermissaoSeNaoExistir("RESPONDER_CHAMADOS", "Responder chamados");
        Permissao fecharChamadosPermissao = criarPermissaoSeNaoExistir("FECHAR_CHAMADOS", "Fechar chamados (resolver)");
        Permissao visualizarHistoricoChamadosPermissao = criarPermissaoSeNaoExistir("VISUALIZAR_HISTORICO_CHAMADOS", "Acessar o histórico de interações com clientes");
        Permissao acessarBaseConhecimentoPermissao = criarPermissaoSeNaoExistir("ACESSAR_BASE_CONHECIMENTO", "Consultar a base de conhecimento");
        Permissao classificarChamadosPermissao = criarPermissaoSeNaoExistir("CLASSIFICAR_CHAMADOS", "Organizar chamados por prioridade ou tipo de problema");

        // **********************************************
        // ********* Permissões de Clientes Finais *******
        // **********************************************
        Permissao usarChatbotPermissao = criarPermissaoSeNaoExistir("USAR_CHATBOT", "Usar o chatbot IA");
        Permissao abrirChamadoPermissao = criarPermissaoSeNaoExistir("ABRIR_CHAMADO", "Abrir um chamado");

        // Cria o usuário Admin se não existir
        User adminUser = userRepository.findByEmail("admin@suportflow.com");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setNome("Administrador do Sistema");
            adminUser.setEmail("admin@suportflow.com");
            adminUser.setSenha(passwordEncoder.encode("suportflow-admin"));
            adminUser.setEmpresa(suportFlowEmpresa); // Associa à empresa SuportFlow
            adminUser.setAtivo(true);
            adminUser.setDataCriacao(LocalDateTime.now());

            // Associa as permissões ao usuário Admin
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

            // Permissões de Gerentes (algumas para exemplificar, ajuste conforme necessário)
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

            // Permissões de Funcionários
            permissoes.add(gerenciarChamadosPermissao);
            permissoes.add(atribuirChamadosPermissao);
            permissoes.add(visualizarChamadosPermissao);
            permissoes.add(responderChamadosPermissao);
            permissoes.add(fecharChamadosPermissao);
            permissoes.add(visualizarHistoricoChamadosPermissao);
            permissoes.add(acessarBaseConhecimentoPermissao);
            permissoes.add(classificarChamadosPermissao);

            // Permissoes de Cliente
            permissoes.add(usarChatbotPermissao);
            permissoes.add(abrirChamadoPermissao);

            adminUser.setPermissoes(permissoes);

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