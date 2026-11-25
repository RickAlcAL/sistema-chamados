package com.senaictti.chamados.DTO;

import com.senaictti.chamados.Entity.statuschamado;
import com.senaictti.chamados.Entity.tipogrupo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class chamadodto {

    // POST /chamados - quem abre é o usuário logado (não precisa mandar id de usuário)
    public record Request(@NotBlank(message = "Título é obrigatorio")
                          @Size(max = 150, message = "Título pode ter no máximo 150 caracteres")
                          String titulo,
                          @NotBlank(message = "Descrição é obrigatoria")
                          @Size(max = 2000, message = "Descrição pode ter no máximo 2000 caracteres")
                          String descricao,
                          Long idPrioridade,   // opcional (padrão: MEDIA)
                          Long idGrupo) {}     // opcional (padrão: N1)

    // PUT /chamados/{id}
    public record AtualizarRequest(@NotBlank(message = "Título é obrigatorio")
                                   @Size(max = 150, message = "Título pode ter no máximo 150 caracteres")
                                   String titulo,
                                   @NotBlank(message = "Descrição é obrigatoria")
                                   @Size(max = 2000, message = "Descrição pode ter no máximo 2000 caracteres")
                                   String descricao) {}

    // PATCH /chamados/{id}/status
    public record StatusRequest(@NotNull(message = "Status é obrigatorio")
                                statuschamado status,
                                @Size(max = 1000, message = "Comentário pode ter no máximo 1000 caracteres")
                                String comentario) {}  // opcional, vai para o histórico

    public record Response(Long id,
                           String titulo,
                           String descricao,
                           statuschamado status,
                           Long idSolicitante,
                           String nomeSolicitante,
                           Long idTecnico,
                           String nomeTecnico,
                           Long idGrupo,
                           String nomeGrupo,
                           tipogrupo tipoGrupo,
                           Long idPrioridade,
                           String nomePrioridade,
                           LocalDateTime dataAbertura,
                           LocalDateTime dataPrazo,
                           LocalDateTime dataEncerramento,
                           Boolean slaCumprido) {}

    // Listas de apoio para o front montar os selects
    public record GrupoResponse(Long id, String nome, tipogrupo tipo) {}
    public record PrioridadeResponse(Long id, String nome, Integer prazoHoras) {}
}
