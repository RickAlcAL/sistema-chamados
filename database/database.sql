CREATE DATABASE IF NOT EXISTS chamados;
USE chamados;

CREATE TABLE usuario (
                         id_usuario     BIGINT       NOT NULL AUTO_INCREMENT,
                         nome_usuario   VARCHAR(255) NOT NULL,
                         idade_usuario  INT,
                         cep_usuario    VARCHAR(8),
                         cpf_usuario    VARCHAR(11)  NOT NULL UNIQUE,
                         senha_usuario  VARCHAR(255)  NOT NULL,
                         PRIMARY KEY (id_usuario)
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
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
    -- Quando a tabela chamado existir, adicionar:
    -- FOREIGN KEY (id_chamado) REFERENCES chamado (id_chamado)
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
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
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
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
);