package com.senaictti.chamados.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class conhecimentodto {

    public record Request(@NotBlank(message = "Título é obrigatorio")
                          String titulo,
                          @NotBlank(message = "Descrição do problema é obrigatoria")
                          String descricaoProblema,
                          @NotBlank(message = "Descrição da solução é obrigatoria")
                          String descricaoSolucao,
                          Long idChamado,          // opcional
                          @NotNull(message = "Autor é obrigatorio")
                          Long idUsuario) {}

    public record Response(Long id,
                           String titulo,
                           String descricaoProblema,
                           String descricaoSolucao,
                           Long idChamado,
                           Long idUsuario,
                           LocalDateTime dataCriacao) {}
}