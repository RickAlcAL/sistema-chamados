package com.senaictti.chamados.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

/*
 Cada linha é um evento do chamado (mudança de status, comentário,
 escalonamento, atendimento remoto ou solução).
 @Immutable: o Hibernate NUNCA faz UPDATE/DELETE nessa tabela.
 É assim que o sistema evita perda de histórico (só insere).
 */
@Entity
@Immutable
@Table(name = "historico_chamado")
public class historicochamado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico")
    private Long id;

    // Chamado que o evento pertence
    @Column(name = "id_chamado", nullable = false, updatable = false)
    private Long idChamado;

    @Column(name = "id_usuario", nullable = false, updatable = false)
    private Long idUsuario;

    // aqui tem STATUS, COMENTARIO, ESCALONAMENTO, ATENDIMENTO_REMOTO, SOLUCAO
    @Column(name = "tipo_evento", nullable = false, updatable = false)
    private String tipoEvento;

    // Preenchidos só quando o evento é uma mudança de status
    @Column(name = "status_anterior", updatable = false)
    private String statusAnterior;

    @Column(name = "status_novo", updatable = false)
    private String statusNovo;

    @Column(name = "descricao", updatable = false, length = 1000)
    private String descricao;

    // Data e hora em que o evento foi registrado
    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdChamado() { return idChamado; }
    public void setIdChamado(Long idChamado) { this.idChamado = idChamado; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public String getStatusAnterior() { return statusAnterior; }
    public void setStatusAnterior(String statusAnterior) { this.statusAnterior = statusAnterior; }
    public String getStatusNovo() { return statusNovo; }
    public void setStatusNovo(String statusNovo) { this.statusNovo = statusNovo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}