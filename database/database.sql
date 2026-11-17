CREATE DATABASE IF NOT EXISTS chamados;
USE chamados;

CREATE TABLE usuario (
                         id_usuario     BIGINT       NOT NULL AUTO_INCREMENT,
                         nome_usuario   VARCHAR(255) NOT NULL,
                         idade_usuario  INT,
                         cep_usuario    VARCHAR(8),
                         cpf_usuario    VARCHAR(11)  NOT NULL UNIQUE,
                         senha_usuario  VARCHAR(255)  NOT NULL,
                         perfil_usuario VARCHAR(20)   NOT NULL DEFAULT 'SOLICITANTE', -- ADMIN, TECNICO ou SOLICITANTE
                         ativo_usuario  BOOLEAN       NOT NULL DEFAULT TRUE,
                         PRIMARY KEY (id_usuario)
);

-- Se você JÁ criou a tabela usuario antes (sem perfil), rode só estas duas linhas:
-- ALTER TABLE usuario ADD COLUMN perfil_usuario VARCHAR(20) NOT NULL DEFAULT 'SOLICITANTE';
-- ALTER TABLE usuario ADD COLUMN ativo_usuario BOOLEAN NOT NULL DEFAULT TRUE;

-- Grupos de atendimento (N1, N2, N3...). Lida pelos indicadores.
-- tipo_grupo: PRODUCAO ou ADMINISTRATIVO. (A aplicação insere N1/N2/N3/ADM sozinha se a tabela estiver vazia)
CREATE TABLE IF NOT EXISTS grupo (
    id_grupo   BIGINT      NOT NULL AUTO_INCREMENT,
    nome_grupo VARCHAR(50) NOT NULL UNIQUE,
    tipo_grupo VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_grupo)
);

-- Prioridades com o prazo (SLA) padrão em horas. Lida pelos indicadores.
-- (A aplicação insere BAIXA/MEDIA/ALTA/CRITICA sozinha se a tabela estiver vazia)
CREATE TABLE IF NOT EXISTS prioridade (
    id_prioridade   BIGINT      NOT NULL AUTO_INCREMENT,
    nome_prioridade VARCHAR(30) NOT NULL UNIQUE,
    prazo_horas     INT         NOT NULL,
    PRIMARY KEY (id_prioridade)
);

-- Chamado (núcleo). Os indicadores leem: id_chamado, id_prioridade, id_grupo,
-- data_abertura, data_encerramento e sla_cumprido.
-- status: ABERTO, EM_ATENDIMENTO ou FECHADO
CREATE TABLE IF NOT EXISTS chamado (
    id_chamado        BIGINT        NOT NULL AUTO_INCREMENT,
    titulo            VARCHAR(150)  NOT NULL,
    descricao         VARCHAR(2000) NOT NULL,
    status            VARCHAR(30)   NOT NULL,
    id_solicitante    BIGINT        NOT NULL,
    id_tecnico        BIGINT,
    id_grupo          BIGINT        NOT NULL,
    id_prioridade     BIGINT        NOT NULL,
    data_abertura     DATETIME      NOT NULL,
    data_prazo        DATETIME      NOT NULL,
    data_encerramento DATETIME,
    sla_cumprido      BOOLEAN,
    PRIMARY KEY (id_chamado),
    FOREIGN KEY (id_solicitante) REFERENCES usuario (id_usuario),
    FOREIGN KEY (id_tecnico)     REFERENCES usuario (id_usuario),
    FOREIGN KEY (id_grupo)       REFERENCES grupo (id_grupo),
    FOREIGN KEY (id_prioridade)  REFERENCES prioridade (id_prioridade)
);


-- Historico De Chamado
-- o histórico só recebe INSERT nunca é alterado nem apagado
CREATE TABLE IF NOT EXISTS historico_chamado (
    id_historico    BIGINT        NOT NULL AUTO_INCREMENT,
    id_chamado      BIGINT        NOT NULL,
    id_usuario      BIGINT        NOT NULL,
    tipo_evento     VARCHAR(30)   NOT NULL,
    status_anterior VARCHAR(30),
    status_novo     VARCHAR(30),
    descricao       VARCHAR(1000),
    data_registro   DATETIME      NOT NULL,
    PRIMARY KEY (id_historico),
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    FOREIGN KEY (id_chamado) REFERENCES chamado (id_chamado)
);

-- essa parte bloqueia UPDATE e DELETE no histórico
DROP TRIGGER IF EXISTS trg_historico_no_update;
DROP TRIGGER IF EXISTS trg_historico_no_delete;
DELIMITER //
CREATE TRIGGER trg_historico_no_update BEFORE UPDATE ON historico_chamado
FOR EACH ROW BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Historico nao pode ser alterado';
END//
CREATE TRIGGER trg_historico_no_delete BEFORE DELETE ON historico_chamado
FOR EACH ROW BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Historico nao pode ser apagado';
END//
DELIMITER ;

-- Solução De Chamados
-- UNIQUE cada chamado tem no máximo uma solução registrada
CREATE TABLE IF NOT EXISTS solucao (
    id_solucao        BIGINT        NOT NULL AUTO_INCREMENT,
    id_chamado        BIGINT        NOT NULL UNIQUE,
    id_usuario        BIGINT        NOT NULL,
    descricao_solucao VARCHAR(2000) NOT NULL,
    data_registro     DATETIME      NOT NULL,
    PRIMARY KEY (id_solucao),
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    FOREIGN KEY (id_chamado) REFERENCES chamado (id_chamado)
);

-- Base De Conhecimento
CREATE TABLE IF NOT EXISTS base_conhecimento (
    id_conhecimento    BIGINT        NOT NULL AUTO_INCREMENT,
    titulo             VARCHAR(255)  NOT NULL,
    descricao_problema VARCHAR(2000) NOT NULL,
    descricao_solucao  VARCHAR(2000) NOT NULL,
    id_chamado         BIGINT,
    id_usuario         BIGINT        NOT NULL,
    data_criacao       DATETIME      NOT NULL,
    PRIMARY KEY (id_conhecimento),
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    FOREIGN KEY (id_chamado) REFERENCES chamado (id_chamado)
);