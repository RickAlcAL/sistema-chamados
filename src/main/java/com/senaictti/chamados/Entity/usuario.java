package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

@Entity
public class usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    @Column(name = "senha_usuario")
    private String senhaUsuario;

    @Column(name = "idade_usuario")
    private Integer idadeUsuario;

    @Column(name = "cep_usuario")
    private String cepUsuario;

    @Column(name = "cpf_usuario")
    private String cpfUsuario;

    // Perfil de acesso (ADMIN, TECNICO ou SOLICITANTE). O default evita erro com usuários antigos.
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_usuario", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'SOLICITANTE'")
    private perfilusuario perfil = perfilusuario.SOLICITANTE;

    // Usuário inativo não consegue fazer login
    @Column(name = "ativo_usuario", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo = true;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public Integer getIdadeUsuario() {
        return idadeUsuario;
    }

    public void setIdadeUsuario(Integer idadeUsuario) {
        this.idadeUsuario = idadeUsuario;
    }

    public String getCepUsuario() {
        return cepUsuario;
    }

    public void setCepUsuario(String cepUsuario) {
        this.cepUsuario = cepUsuario;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public perfilusuario getPerfil() {
        return perfil;
    }

    public void setPerfil(perfilusuario perfil) {
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
