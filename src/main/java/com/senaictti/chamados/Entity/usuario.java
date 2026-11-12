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
}
