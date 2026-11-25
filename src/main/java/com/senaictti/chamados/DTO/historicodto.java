package com.senaictti.chamados.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class historicodto {

    // Dados que chegam na API para registrar um evento
    public record Request(@NotNull(message = "Chamado é obrigatorio")
                          Long idChamado,
                          @NotNull(message = "Autor é obrigatorio")
                          Long idUsuario,
                          // Só aceita um destes tipos de evento
                          @NotBlank(message = "Tipo do evento é obrigatorio")
                          @Pattern(regexp = "STATUS|COMENTARIO|ESCALONAMENTO|ATENDIMENTO_REMOTO|SOLUCAO",
                                  message = "Tipo deve ser STATUS, COMENTARIO, ESCALONAMENTO, ATENDIMENTO_REMOTO ou SOLUCAO")
                          String tipoEvento,
                          String statusAnterior,   // opcional (só em mudança de status)
                          String statusNovo,       // opcional (só em mudança de status)
                          @NotBlank(message = "Descrição é obrigatoria")
                          String descricao) {}

    // Dados que a API devolve
    public record Response(Long id,
                           Long idChamado,
                           Long idUsuario,
                           String tipoEvento,
                           String statusAnterior,
                           String statusNovo,
                           String descricao,
                           LocalDateTime dataRegistro) {}
}