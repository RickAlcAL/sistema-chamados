package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.solucaodto;
import com.senaictti.chamados.Service.solucaoservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solucoes")
public class solucaocontroller {

    private final solucaoservice solucaoService;

    public solucaocontroller(solucaoservice solucaoService) {
        this.solucaoService = solucaoService;
    }

    // POST /solucoes - registra a solução (obrigatória antes de fechar o chamado)
    @PostMapping
    public ResponseEntity<solucaodto.Response> registrar(@RequestBody @Valid solucaodto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solucaoService.registrar(request));
    }

    // GET /solucoes/chamado/5 -> solução do chamado 5
    @GetMapping("/chamado/{idChamado}")
    public ResponseEntity<solucaodto.Response> buscarPorChamado(@PathVariable Long idChamado) {
        return ResponseEntity.ok(solucaoService.buscarPorChamado(idChamado));
    }
}