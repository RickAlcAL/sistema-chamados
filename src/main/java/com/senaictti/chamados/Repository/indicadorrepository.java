package com.senaictti.chamados.Repository;

import com.senaictti.chamados.DTO.indicadordto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 estas consultas leem as tabelas
 Elas assumem estes nomes
 chamado(id_chamado, id_prioridade, id_grupo, data_abertura, data_encerramento, sla_cumprido)
 grupo(id_grupo, nome_grupo, tipo_grupo)   -> tipo_grupo = 'PRODUCAO' ou 'ADMINISTRATIVO'
 prioridade(id_prioridade, nome_prioridade)
 Usamos JdbcTemplate (SQL direto) porque o indicador junta várias tabelas
 que pertencem a outras pessoas, e assim não dependemos das entidades delas.
 */
@Repository
public class indicadorrepository {

    private final JdbcTemplate jdbc;

    public indicadorrepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Tempo médio em minutos entre abertura e encerramento
    private static final String MEDIA =
            "AVG(TIMESTAMPDIFF(MINUTE, c.data_abertura, c.data_encerramento))";

    // Chamados produtivos x administrativos quantidade e tempo médio
    public List<indicadordto.Item> porTipo() {
        return jdbc.query(
                "SELECT g.tipo_grupo AS agrupamento, COUNT(*) AS quantidade, " + MEDIA + " AS tempo_medio " +
                        "FROM chamado c JOIN grupo g ON g.id_grupo = c.id_grupo " +
                        "GROUP BY g.tipo_grupo",
                (rs, i) -> new indicadordto.Item(rs.getString("agrupamento"),
                        rs.getLong("quantidade"), rs.getDouble("tempo_medio")));
    }

    // Por prioridade
    public List<indicadordto.Item> porPrioridade() {
        return jdbc.query(
                "SELECT p.nome_prioridade AS agrupamento, COUNT(*) AS quantidade, " + MEDIA + " AS tempo_medio " +
                        "FROM chamado c JOIN prioridade p ON p.id_prioridade = c.id_prioridade " +
                        "GROUP BY p.nome_prioridade",
                (rs, i) -> new indicadordto.Item(rs.getString("agrupamento"),
                        rs.getLong("quantidade"), rs.getDouble("tempo_medio")));
    }

    // Por grupo (N1, N2, N3)
    public List<indicadordto.Item> porGrupo() {
        return jdbc.query(
                "SELECT g.nome_grupo AS agrupamento, COUNT(*) AS quantidade, " + MEDIA + " AS tempo_medio " +
                        "FROM chamado c JOIN grupo g ON g.id_grupo = c.id_grupo " +
                        "GROUP BY g.nome_grupo",
                (rs, i) -> new indicadordto.Item(rs.getString("agrupamento"),
                        rs.getLong("quantidade"), rs.getDouble("tempo_medio")));
    }

    // SLA cumprido: total, cumpridos, estourados e percentual
    public indicadordto.Sla sla() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) AS total, " +
                        "COALESCE(SUM(CASE WHEN sla_cumprido = 1 THEN 1 ELSE 0 END), 0) AS cumpridos " +
                        "FROM chamado",
                (rs, i) -> {
                    long total = rs.getLong("total");
                    long cumpridos = rs.getLong("cumpridos");
                    double pct = total == 0 ? 0.0 : (cumpridos * 100.0) / total;
                    return new indicadordto.Sla(total, cumpridos, total - cumpridos, pct);
                });
    }
}