package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

/*
 Prioridade do chamado. prazoHoras é o prazo (SLA) padrão para resolver.
 A tabela é lida pelos indicadores (indicadorrepository), então não mude os nomes das colunas.
 */
@Entity
@Table(name = "prioridade")
public class prioridade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prioridade")
    private Long id;

    @Column(name = "nome_prioridade", nullable = false, unique = true, length = 30)
    private String nomePrioridade;

    @Column(name = "prazo_horas", nullable = false)
    private Integer prazoHoras;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomePrioridade() { return nomePrioridade; }
    public void setNomePrioridade(String nomePrioridade) { this.nomePrioridade = nomePrioridade; }
    public Integer getPrazoHoras() { return prazoHoras; }
    public void setPrazoHoras(Integer prazoHoras) { this.prazoHoras = prazoHoras; }
}
