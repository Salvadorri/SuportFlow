-- Criar tabela de empresas
CREATE TABLE empresas (
    empresa_id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    endereco VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cnpj VARCHAR(18) UNIQUE,
    data_criacao TIMESTAMP
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
    data_criacao TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
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
    cpf_cnpj VARCHAR(18) UNIQUE,
    data_cadastro TIMESTAMP,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Criar tabela de chamados
CREATE TABLE chamados (
    chamado_id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    usuario_id BIGINT,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    prioridade VARCHAR(20) NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP,
    avaliacao INTEGER,
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id)
);

-- Criar tabela de refresh tokens
CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    user_id BIGINT,
    cliente_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    CONSTRAINT check_token_association CHECK ((user_id IS NULL AND cliente_id IS NOT NULL) OR (user_id IS NOT NULL AND cliente_id IS NULL))
    -- Sem restrição única no user_id ou cliente_id
);

-- Criar índices para melhorar performance
CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_cliente_email ON clientes(email);
CREATE INDEX idx_cliente_cpf_cnpj ON clientes(cpf_cnpj);
CREATE INDEX idx_chamado_cliente ON chamados(cliente_id);
CREATE INDEX idx_chamado_usuario ON chamados(usuario_id);
CREATE INDEX idx_chamado_status ON chamados(status);
CREATE INDEX idx_refresh_token ON refresh_tokens(token);