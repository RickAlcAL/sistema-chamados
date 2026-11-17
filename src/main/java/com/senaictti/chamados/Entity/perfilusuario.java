package com.senaictti.chamados.Entity;

/*
 Perfis (roles) do sistema.
 SOLICITANTE: usuário comum, abre e acompanha os próprios chamados (perfil padrão do cadastro).
 TECNICO:     atende chamados.
 ADMIN:       gerencia usuários e perfis.
 No Spring Security cada perfil vira a authority "ROLE_<NOME>", por exemplo hasRole('ADMIN').
 */
public enum perfilusuario {
    ADMIN,
    TECNICO,
    SOLICITANTE
}
