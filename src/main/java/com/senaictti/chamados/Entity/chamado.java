package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
 Núcleo do sistema. Os nomes de tabela/colunas (id_chamado, id_prioridade, id_grupo,
 data_abertura, data_encerramento, sla_cumprido) são os que o indicadorrepository lê.
 */
@Entity
@Table(name = "chamado")
public class chamado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chamado")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descricao", nullable = false, length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private statuschamado status;

    // Quem abriu o chamado
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitante", nullable = false)
    private usuario solicitante;

    // Técnico responsável (null enquanto ninguém assumiu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    private usuario tecnico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_grupo", nullable = false)
    private grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prioridade", nullable = false)
    private prioridade prioridade;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    // abertura + prazo da prioridade
    @Column(name = "data_prazo", nullable = false)
    private LocalDateTime dataPrazo;

    @Column(name = "data_encerramento")
    private LocalDateTime dataEncerramento;

    // Preenchido só ao fechar: true se fechou dentro do prazo
    @Column(name = "sla_cumprido")
    private Boolean slaCumprido;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public statuschamado getStatus() { return status; }
    public void setStatus(statuschamado status) { this.status = status; }
    public usuario getSolicitante() { return solicitante; }
    public void setSolicitante(usuario solicitante) { this.solicitante = solicitante; }
    public usuario getTecnico() { return tecnico; }
    public void setTecnico(usuario tecnico) { this.tecnico = tecnico; }
    public grupo getGrupo() { return grupo; }
    public void setGrupo(grupo grupo) { this.grupo = grupo; }
    public prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(prioridade prioridade) { this.prioridade = prioridade; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
    public LocalDateTime getDataPrazo() { return dataPrazo; }
    public void setDataPrazo(LocalDateTime dataPrazo) { this.dataPrazo = dataPrazo; }
    public LocalDateTime getDataEncerramento() { return dataEncerramento; }
    public void setDataEncerramento(LocalDateTime dataEncerramento) { this.dataEncerramento = dataEncerramento; }
    public Boolean getSlaCumprido() { return slaCumprido; }
    public void setSlaCumprido(Boolean slaCumprido) { this.slaCumprido = slaCumprido; }
}
