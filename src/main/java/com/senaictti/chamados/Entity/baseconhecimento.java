package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
 Artigos com problema + solução, para consulta em problemas recorrentes
 pode ser ligado a um chamado.
 */
@Entity
@Table(name = "base_conhecimento")
public class baseconhecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conhecimento")
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao_problema", nullable = false, length = 2000)
    private String descricaoProblema;

    @Column(name = "descricao_solucao", nullable = false, length = 2000)
    private String descricaoSolucao;

    // Opcional: chamado que originou o artigo (pode ser null)
    @Column(name = "id_chamado")
    private Long idChamado;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }
    public String getDescricaoSolucao() { return descricaoSolucao; }
    public void setDescricaoSolucao(String descricaoSolucao) { this.descricaoSolucao = descricaoSolucao; }
    public Long getIdChamado() { return idChamado; }
    public void setIdChamado(Long idChamado) { this.idChamado = idChamado; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}