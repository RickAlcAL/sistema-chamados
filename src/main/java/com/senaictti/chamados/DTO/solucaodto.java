package com.senaictti.chamados.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class solucaodto {

    public record Request(@NotNull(message = "Chamado é obrigatorio")
                          Long idChamado,
                          @NotNull(message = "Autor é obrigatorio")
                          Long idUsuario,
                          @NotBlank(message = "A solução é obrigatoria")
                          String descricaoSolucao) {}

    public record Response(Long id,
                           Long idChamado,
                           Long idUsuario,
                           String descricaoSolucao,
                           LocalDateTime dataRegistro) {}
}