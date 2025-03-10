-- V1__criar_tabelas_e_indices.sql

-- Criar tabela de empresas
CREATE TABLE empresas (
    empresa_id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    endereco VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cnpj VARCHAR(20) UNIQUE,  -- Changed to VARCHAR(20) to accommodate formatted CNPJs (e.g., "XX.XXX.XXX/YYYY-ZZ")
    data_criacao TIMESTAMP WITHOUT TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC') --Added default and UTC
);

-- Criar tabela de permissões
CREATE TABLE permissoes (
    permissao_id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

-- Criar tabela de usuários
CREATE TABLE usuarios (
    usuario_id SERIAL PRIMARY KEY,
    empresa_id BIGINT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP WITHOUT TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'), --Added default and UTC
    ativo BOOLEAN DEFAULT TRUE,
    telefone VARCHAR(20), -- Added missing columns
    cpf_cnpj VARCHAR(20) UNIQUE, -- Added missing columns and adjusted length for CPF/CNPJ
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Criar tabela de relacionamento entre usuários e permissões
CREATE TABLE usuario_permissao (
    usuario_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, permissao_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (permissao_id) REFERENCES permissoes(permissao_id)
);

-- Criar tabela de clientes
CREATE TABLE clientes (
    cliente_id SERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(20),
    cpf_cnpj VARCHAR(20) UNIQUE, -- Adjusted length for CPF/CNPJ
    data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'), --Added default and UTC
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);
-- Criar tabela de chamados
CREATE TABLE chamados (
    chamado_id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    usuario_id BIGINT NULL, -- Allow NULL
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    prioridade VARCHAR(20) NOT NULL,
    data_abertura TIMESTAMP WITHOUT TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'),
    data_fechamento TIMESTAMP WITHOUT TIME ZONE, -- Keep it nullable for open tickets.
    avaliacao INTEGER,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE SET NULL -- Added ON DELETE SET NULL
);

-- Criar tabela chat_mensagens
CREATE TABLE chat_mensagens (
    mensagem_id SERIAL PRIMARY KEY,
    chamado_id BIGINT NOT NULL,
    usuario_id BIGINT NULL,
    cliente_id BIGINT NULL,
    mensagem TEXT NOT NULL,
    data_envio TIMESTAMP WITHOUT TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'),
    caminho_arquivo VARCHAR(255),
    FOREIGN KEY (chamado_id) REFERENCES chamados(chamado_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE SET NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
     CONSTRAINT check_mensagem_association CHECK ((usuario_id IS NULL AND cliente_id IS NOT NULL) OR (usuario_id IS NOT NULL AND cliente_id IS NULL) OR (usuario_id IS NULL AND cliente_id IS NULL))
);

-- Criar tabela de refresh tokens
CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id BIGINT,
    cliente_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES usuarios(usuario_id) ON DELETE CASCADE,  -- Changed to CASCADE
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    CONSTRAINT check_token_association CHECK ((user_id IS NULL AND cliente_id IS NOT NULL) OR (user_id IS NOT NULL AND cliente_id IS NULL))
    -- Sem restrição única no user_id ou cliente_id
);

-- Índices (melhorados para cobrir mais casos de uso)
CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_usuario_empresa ON usuarios(empresa_id); -- Added index
CREATE INDEX idx_cliente_email ON clientes(email);
CREATE INDEX idx_cliente_cpf_cnpj ON clientes(cpf_cnpj);
CREATE INDEX idx_cliente_empresa ON clientes(empresa_id);  -- Added index
CREATE INDEX idx_chamado_cliente ON chamados(cliente_id);
CREATE INDEX idx_chamado_usuario ON chamados(usuario_id);
CREATE INDEX idx_chamado_status ON chamados(status);
CREATE INDEX idx_chamado_categoria ON chamados(categoria); -- For filtering by category
CREATE INDEX idx_chamado_prioridade ON chamados(prioridade); -- For filtering by priority
CREATE INDEX idx_chamado_data_abertura ON chamados(data_abertura); -- Crucial for date range queries
CREATE INDEX idx_chamado_data_fechamento ON chamados(data_fechamento); -- For querying closed tickets by date.
CREATE INDEX idx_refresh_token ON refresh_tokens(token);
CREATE INDEX idx_chat_mensagem_chamado ON chat_mensagens(chamado_id); -- Added index for chat messages
CREATE INDEX idx_chat_mensagem_data_envio ON chat_mensagens(data_envio); -- Added index for chat messages by date

-- Add unique constraint to usuarios for both email and empresa_id
ALTER TABLE usuarios ADD CONSTRAINT unique_email_empresa UNIQUE (email, empresa_id);

-- Add unique constraint to clientes for both email and empresa_id, and cpf_cnpj and empresa_id
ALTER TABLE clientes ADD CONSTRAINT unique_email_empresa_clientes UNIQUE (email, empresa_id);
ALTER TABLE clientes ADD CONSTRAINT unique_cpf_cnpj_empresa_clientes UNIQUE (cpf_cnpj, empresa_id);
