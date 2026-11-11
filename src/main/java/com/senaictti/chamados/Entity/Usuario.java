package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nomeUsuario")
    private String nomeUsuario;

    @Column(name = "idadeUsuario")
    private Integer idadeUsuario;

    @Column(name = "cepUsuario")
    private String cepUsuario;

    @Column(name = "cpfUsuario")
    private String cpfUsuario;

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
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
