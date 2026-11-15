package com.senaictti.chamados.DTO;

public class indicadordto {

    // agrupamento = nome do grupo/prioridade/tipo
    public record Item(String agrupamento,
                       Long quantidade,
                       Double tempoMedioMinutos) {}

    // Resumo do SLA: quantos chamados cumpriram o prazo
    public record Sla(Long total,
                      Long cumpridos,
                      Long estourados,
                      Double percentualCumprido) {}
}