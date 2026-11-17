package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.usuariodto;
import com.senaictti.chamados.Security.usuariodetails;
import com.senaictti.chamados.Service.usuarioservice;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/*
 Autenticação: o login é feito com HTTP Basic (CPF como usuário + senha).
 GET /auth/me serve para "entrar": se as credenciais estiverem certas devolve os dados
 e o perfil do usuário; se estiverem erradas, devolve 401.
 */
@RestController
@RequestMapping("/auth")
public class authcontroller {

    private final usuarioservice usuarioService;

    public authcontroller(usuarioservice usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<usuariodto.Response> me(@AuthenticationPrincipal usuariodetails logado) {
        return ResponseEntity.ok(usuarioService.me(logado));
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> alterarSenha(@AuthenticationPrincipal usuariodetails logado,
                                             @RequestBody @Valid usuariodto.SenhaRequest request) {
        usuarioService.alterarSenha(logado, request);
        return ResponseEntity.noContent().build();
    }
}
