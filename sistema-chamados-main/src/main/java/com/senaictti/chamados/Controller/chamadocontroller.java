package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.chamadodto;
import com.senaictti.chamados.Entity.statuschamado;
import com.senaictti.chamados.Security.usuariodetails;
import com.senaictti.chamados.Service.chamadoservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 Endpoints do núcleo de chamados.
 Não existe DELETE: chamado nunca é apagado (o histórico depende dele).
 */
@RestController
@RequestMapping("/chamados")
public class chamadocontroller {

    private final chamadoservice chamadoService;

    public chamadocontroller(chamadoservice chamadoService) {
        this.chamadoService = chamadoService;
    }

    // POST /chamados - qualquer usuário logado abre um chamado
    @PostMapping
    public ResponseEntity<chamadodto.Response> abrir(@RequestBody @Valid chamadodto.Request request,
                                                     @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chamadoService.abrir(request, logado));
    }

    // GET /chamados ou /chamados?status=ABERTO
    @GetMapping
    public ResponseEntity<List<chamadodto.Response>> listar(
            @RequestParam(name = "status", required = false) statuschamado status,
            @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(chamadoService.listar(status, logado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<chamadodto.Response> buscar(@PathVariable Long id,
                                                      @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(chamadoService.buscar(id, logado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<chamadodto.Response> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid chamadodto.AtualizarRequest request,
                                                         @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(chamadoService.atualizar(id, request, logado));
    }

    // PATCH /chamados/5/assumir - técnico assume o chamado (vira EM_ATENDIMENTO)
    @PatchMapping("/{id}/assumir")
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    public ResponseEntity<chamadodto.Response> assumir(@PathVariable Long id,
                                                       @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(chamadoService.assumir(id, logado));
    }

    // PATCH /chamados/5/status - muda o status (para FECHADO precisa de solução)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    public ResponseEntity<chamadodto.Response> mudarStatus(@PathVariable Long id,
                                                           @RequestBody @Valid chamadodto.StatusRequest request,
                                                           @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(chamadoService.mudarStatus(id, request, logado));
    }
}
