package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
 Solução registrada para um chamado. O chamado só pode ser fechado
 depois que existir uma linha aqui
 */
@Entity
@Table(name = "solucao")
public class solucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solucao")
    private Long id;

    // chamado tem no máximo uma solução
    @Column(name = "id_chamado", nullable = false, unique = true)
    private Long idChamado;

    // Técnico que registrou a solução
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "descricao_solucao", nullable = false, length = 2000)
    private String descricaoSolucao;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdChamado() { return idChamado; }
    public void setIdChamado(Long idChamado) { this.idChamado = idChamado; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getDescricaoSolucao() { return descricaoSolucao; }
    public void setDescricaoSolucao(String descricaoSolucao) { this.descricaoSolucao = descricaoSolucao; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}