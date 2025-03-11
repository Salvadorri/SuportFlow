// src/main/java/com/suportflow/backend/config/DataInitializer.java
package com.suportflow.backend.config;

import com.suportflow.backend.model.*;
import com.suportflow.backend.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private final EmpresaRepository empresaRepository;
    private final PermissaoRepository permissaoRepository;
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final ChamadoRepository chamadoRepository;
    private final ChatMensagemRepository chatMensagemRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    private static final String DEFAULT_CLIENT_PASSWORD = "SenhaPadrao123!";

    public DataInitializer(EmpresaRepository empresaRepository, PermissaoRepository permissaoRepository,
                           UserRepository userRepository, ClienteRepository clienteRepository,
                           ChamadoRepository chamadoRepository, ChatMensagemRepository chatMensagemRepository,
                           PasswordEncoder passwordEncoder, @Value("${admin.email}") String adminEmail,
                           @Value("${admin.password}") String adminPassword) {
        this.empresaRepository = empresaRepository;
        this.permissaoRepository = permissaoRepository;
        this.userRepository = userRepository;
        this.clienteRepository = clienteRepository;
        this.chamadoRepository = chamadoRepository;
        this.chatMensagemRepository = chatMensagemRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }
    private LocalDateTime randomPastDateTime() {
        Random random = new Random();
        int daysAgo = random.nextInt(30); // Up to 30 days ago
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        return LocalDateTime.now().minusDays(daysAgo).withHour(hour).withMinute(minute);
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

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

        Permissao adminPermissao = criarPermissaoSeNaoExistir("ADMIN", "Permissão de Administrador");
        Permissao gerentePermissao = criarPermissaoSeNaoExistir("GERENTE", "Permissão de Gerente");
        Permissao atendentePermissao = criarPermissaoSeNaoExistir("ATENDENTE", "Permissão de Atendente");

        User adminUser = criarUsuarioSeNaoExistir("Administrador do Sistema", adminEmail, adminPassword, suportFlowEmpresa, Set.of(adminPermissao));
        User gerenteUser = criarUsuarioSeNaoExistir("Gerente", "gerente@suportflow.com", "senhaGerente", suportFlowEmpresa, Set.of(gerentePermissao));
        User atendenteUser = criarUsuarioSeNaoExistir("Atendente", "atendente@suportflow.com", "senhaAtendente", suportFlowEmpresa, Set.of(atendentePermissao));


        Cliente cliente1 = criarClienteSeNaoExistir("cliente1@example.com", "12345678901", "Cliente 1", suportFlowEmpresa);
        Cliente cliente2 = criarClienteSeNaoExistir("cliente2@example.com", "12345678901234", "Cliente 2", suportFlowEmpresa);

        // Example Chamados and ChatMensagens
        Chamado chamado1 = criarChamado(cliente1, atendenteUser, "Problema de login", "Não consigo acessar minha conta.", CategoriaChamado.SUPORTE_TECNICO, StatusChamado.EM_ANDAMENTO, PrioridadeChamado.ALTA);
        createChatMensagem(chamado1, cliente1, null, "Tentei redefinir a senha, mas não recebi o email.", randomPastDateTime());
        createChatMensagem(chamado1, null, atendenteUser, "Por favor, verifique sua pasta de spam.", randomPastDateTime());

        Chamado chamado2 = criarChamado(cliente2, null, "Dúvida sobre funcionalidade", "Como faço para gerar um relatório?", CategoriaChamado.DUVIDA, StatusChamado.ABERTO, PrioridadeChamado.MEDIA);
        createChatMensagem(chamado2, cliente2, null, "Preciso de um relatório detalhado das minhas atividades.", randomPastDateTime());

        Chamado chamado3 = criarChamado(cliente1, atendenteUser, "Erro no sistema", "O sistema trava ao tentar salvar.", CategoriaChamado.SUPORTE_TECNICO, StatusChamado.RESOLVIDO, PrioridadeChamado.URGENTE);
        createChatMensagem(chamado3, null, atendenteUser, "Identificamos o problema e já corrigimos. Pode tentar novamente?", randomPastDateTime());
        createChatMensagem(chamado3, cliente1, null, "Funcionou! Obrigado!", randomPastDateTime());
        chamado3.setDataFechamento(LocalDateTime.now()); // Set closing date
        chamadoRepository.save(chamado3); // Update to save the closing date.

    }

    @Transactional
    protected Permissao criarPermissaoSeNaoExistir(String nome, String descricao) {
        return permissaoRepository.findByNome(nome)
                .orElseGet(() -> {
                    Permissao newPermissao = new Permissao();
                    newPermissao.setNome(nome);
                    newPermissao.setDescricao(descricao);
                    return permissaoRepository.save(newPermissao);
                });
    }

    @Transactional
    protected User criarUsuarioSeNaoExistir(String nome, String email, String senha, Empresa empresa, Set<Permissao> permissoes) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setNome(nome);
            newUser.setEmail(email);
            newUser.setSenha(passwordEncoder.encode(senha));
            newUser.setEmpresa(empresa);
            newUser.setAtivo(true);
            newUser.setDataCriacao(LocalDateTime.now());
            newUser.setPermissoes(permissoes);
            return userRepository.save(newUser);
        });
    }

    @Transactional
    protected Cliente criarClienteSeNaoExistir(String email, String cpfCnpj, String nome, Empresa empresa) {
        if (!clienteRepository.existsByEmail(email) && !clienteRepository.existsByCpfCnpj(cpfCnpj)) {
            Cliente cliente = new Cliente();
            cliente.setEmail(email);
            cliente.setCpfCnpj(cpfCnpj);
            cliente.setSenha(passwordEncoder.encode(DEFAULT_CLIENT_PASSWORD)); // Senha padrão criptografada
            cliente.setNome(nome);
            cliente.setEmpresa(empresa);
            cliente.setDataCadastro(LocalDateTime.now());
            return clienteRepository.save(cliente);
        }
        return clienteRepository.findByEmail(email).orElse(null);
    }
    @Transactional
    protected Chamado criarChamado(Cliente cliente, User atendente, String titulo, String descricao,
                                   CategoriaChamado categoria, StatusChamado status, PrioridadeChamado prioridade) {

        if (cliente == null ) {
            throw new IllegalArgumentException("Cliente não pode ser nulo ao criar um chamado.");
        }

        Chamado chamado = new Chamado();
        chamado.setCliente(cliente);
        chamado.setAtendente(atendente);
        chamado.setTitulo(titulo);
        chamado.setDescricao(descricao);
        chamado.setCategoria(categoria);
        chamado.setStatus(status);
        chamado.setPrioridade(prioridade);
        chamado.setDataAbertura(randomPastDateTime());
        return chamadoRepository.save(chamado);
    }

    @Transactional
    protected ChatMensagem createChatMensagem(Chamado chamado, Cliente cliente, User usuario, String mensagem, LocalDateTime dataEnvio) {
        if (chamado == null) {
            throw new IllegalArgumentException("Chamado não pode ser nulo ao criar uma mensagem de chat.");
        }
        if((cliente == null && usuario == null) || (cliente != null && usuario != null)){
            throw new IllegalArgumentException("A mensagem deve ser associada a um cliente ou a um usuário, exclusivamente.");
        }

        ChatMensagem chatMensagem = new ChatMensagem();
        chatMensagem.setChamado(chamado);
        chatMensagem.setCliente(cliente);
        chatMensagem.setUsuario(usuario);
        chatMensagem.setMensagem(mensagem);
        chatMensagem.setDataEnvio(dataEnvio);
        return chatMensagemRepository.save(chatMensagem);
    }
}