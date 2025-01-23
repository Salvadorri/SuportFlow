-- V1 - Criação das tabelas iniciais do projeto SupportFlow AI

-- Tabela Empresas
CREATE TABLE empresas (
    empresa_id SERIAL PRIMARY KEY,
    nome VARCHAR(255) UNIQUE NOT NULL,
    endereco TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    cnpj VARCHAR(14) UNIQUE, -- CNPJ que pode ser nulo
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Cargos
CREATE TABLE cargos (
    cargo_id SERIAL PRIMARY KEY,
    empresa_id INT,
    nome_cargo VARCHAR(255) NOT NULL,
    descricao TEXT,
    is_admin BOOLEAN DEFAULT FALSE,
    is_gerente BOOLEAN DEFAULT FALSE,
    pode_ver_todos_chamados BOOLEAN DEFAULT FALSE,
    pode_criar_chamados BOOLEAN DEFAULT FALSE,
    pode_responder_chamados BOOLEAN DEFAULT FALSE,
    pode_fechar_chamados BOOLEAN DEFAULT FALSE,
    pode_atribuir_chamados BOOLEAN DEFAULT FALSE,
    pode_gerenciar_base_conhecimento BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Tabela Usuarios
CREATE TABLE usuarios (
    usuario_id SERIAL PRIMARY KEY,
    empresa_id INT,
    cargo_id INT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id),
    FOREIGN KEY (cargo_id) REFERENCES cargos(cargo_id)
);

-- Tabela Clientes
CREATE TABLE clientes (
    cliente_id INT PRIMARY KEY,
    empresa_id INT NOT NULL,
    telefone VARCHAR(20),
    outras_informacoes JSONB,
    FOREIGN KEY (cliente_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Tabela Chamados
CREATE TABLE chamados (
    chamado_id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    funcionario_id INT,
    empresa_id INT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    status VARCHAR(255) NOT NULL DEFAULT 'aberto',
    prioridade VARCHAR(255) NOT NULL DEFAULT 'baixa',
    categoria VARCHAR(255),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    data_fechamento TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    FOREIGN KEY (funcionario_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Tabela MensagensChamado
CREATE TABLE mensagens_chamado (
    mensagem_id SERIAL PRIMARY KEY,
    chamado_id INT NOT NULL,
    usuario_id INT NOT NULL,
    mensagem TEXT NOT NULL,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chamado_id) REFERENCES chamados(chamado_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id)
);

-- Tabela BaseConhecimento
CREATE TABLE base_conhecimento (
    artigo_id SERIAL PRIMARY KEY,
    empresa_id INT,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    palavras_chave TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Tabela Feedbacks
CREATE TABLE feedbacks (
    feedback_id SERIAL PRIMARY KEY,
    chamado_id INT NOT NULL,
    cliente_id INT NOT NULL,
    avaliacao INT NOT NULL,
    comentario TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chamado_id) REFERENCES chamados(chamado_id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id)
);

-- Índices (opcional, mas recomendado para melhor desempenho)
CREATE INDEX idx_empresa_id ON cargos (empresa_id);
CREATE INDEX idx_empresa_id_usuarios ON usuarios (empresa_id);
CREATE INDEX idx_cargo_id ON usuarios (cargo_id);
CREATE INDEX idx_empresa_id_clientes ON clientes (empresa_id);
CREATE INDEX idx_cliente_id_chamados ON chamados (cliente_id);
CREATE INDEX idx_funcionario_id_chamados ON chamados (funcionario_id);
CREATE INDEX idx_empresa_id_chamados ON chamados (empresa_id);
CREATE INDEX idx_chamado_id_mensagens ON mensagens_chamado (chamado_id);
CREATE INDEX idx_usuario_id_mensagens ON mensagens_chamado (usuario_id);
CREATE INDEX idx_empresa_id_base_conhecimento ON base_conhecimento (empresa_id);
CREATE INDEX idx_chamado_id_feedbacks ON feedbacks (chamado_id);
CREATE INDEX idx_cliente_id_feedbacks ON feedbacks (cliente_id);