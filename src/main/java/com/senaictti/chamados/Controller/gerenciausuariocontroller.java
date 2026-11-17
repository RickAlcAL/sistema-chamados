package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.usuariodto;
import com.senaictti.chamados.Security.usuariodetails;
import com.senaictti.chamados.Service.usuarioservice;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 Gerenciamento de usuários e perfis.
 - GET /usuarios, PATCH /usuarios/{id}/perfil e /status: só ADMIN
 - GET e PUT /usuarios/{id}: ADMIN ou o próprio usuário (regra dentro do service)
 */
@RestController
@RequestMapping("/usuarios")
public class gerenciausuariocontroller {

    private final usuarioservice usuarioService;

    public gerenciausuariocontroller(usuarioservice usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<usuariodto.Response>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<usuariodto.Response> buscar(@PathVariable Long id,
                                                      @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(usuarioService.buscar(id, logado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<usuariodto.Response> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid usuariodto.AtualizarRequest request,
                                                         @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request, logado));
    }

    @PatchMapping("/{id}/perfil")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<usuariodto.Response> alterarPerfil(@PathVariable Long id,
                                                             @RequestBody @Valid usuariodto.PerfilRequest request,
                                                             @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(usuarioService.alterarPerfil(id, request, logado));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<usuariodto.Response> alterarStatus(@PathVariable Long id,
                                                             @RequestBody @Valid usuariodto.StatusRequest request,
                                                             @AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(usuarioService.alterarStatus(id, request, logado));
    }
}
