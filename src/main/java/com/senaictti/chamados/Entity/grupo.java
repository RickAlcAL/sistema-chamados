package com.senaictti.chamados.Entity;

import jakarta.persistence.*;

/*
 Grupo de atendimento (N1, N2, N3...). O tipo diz se o grupo é de PRODUCAO ou ADMINISTRATIVO.
 A tabela é lida pelos indicadores (indicadorrepository), então não mude os nomes das colunas.
 */
@Entity
@Table(name = "grupo")
public class grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long id;

    @Column(name = "nome_grupo", nullable = false, unique = true, length = 50)
    private String nomeGrupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_grupo", nullable = false, length = 20)
    private tipogrupo tipoGrupo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeGrupo() { return nomeGrupo; }
    public void setNomeGrupo(String nomeGrupo) { this.nomeGrupo = nomeGrupo; }
    public tipogrupo getTipoGrupo() { return tipoGrupo; }
    public void setTipoGrupo(tipogrupo tipoGrupo) { this.tipoGrupo = tipoGrupo; }
}
