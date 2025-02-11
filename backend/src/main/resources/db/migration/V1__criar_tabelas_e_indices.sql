-- V1 - Criação das tabelas iniciais do projeto SupportFlow AI

-- Tabela Empresas
CREATE TABLE empresas (
    empresa_id SERIAL PRIMARY KEY,
    nome VARCHAR(255) UNIQUE NOT NULL,
    endereco TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    cnpj VARCHAR(14) UNIQUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Permissoes
CREATE TABLE permissoes (
    permissao_id SERIAL PRIMARY KEY,
    nome VARCHAR(255) UNIQUE NOT NULL,
    descricao TEXT
);

-- Tabela Usuarios
CREATE TABLE usuarios (
    usuario_id SERIAL PRIMARY KEY,
    empresa_id INT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (empresa_id) REFERENCES empresas(empresa_id)
);

-- Tabela Usuario_Permissao (associativa entre Usuários e Permissoes)
CREATE TABLE usuario_permissao (
    usuario_id INT,
    permissao_id INT,
    PRIMARY KEY (usuario_id, permissao_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (permissao_id) REFERENCES permissoes(permissao_id)
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

