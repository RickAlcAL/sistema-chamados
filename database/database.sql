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