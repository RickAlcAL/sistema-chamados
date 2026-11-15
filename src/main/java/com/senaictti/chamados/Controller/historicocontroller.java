package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.historicodto;
import com.senaictti.chamados.Service.historicoservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 Endpoints do histórico.
 Só existem POST e GET: NÃO há PUT nem DELETE, então o histórico
 nunca é alterado ou apagado pela API.
 */
@RestController
@RequestMapping("/historico")
public class historicocontroller {

    private final historicoservice historicoService;

    public historicocontroller(historicoservice historicoService) {
        this.historicoService = historicoService;
    }

    // POST /historico - registra um evento (comentário, atendimento remoto, etc.)
    @PostMapping
    public ResponseEntity<historicodto.Response> registrar(@RequestBody @Valid historicodto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historicoService.registrar(request));
    }

    // GET /historico/chamado/5 - histórico completo do chamado 5
    @GetMapping("/chamado/{idChamado}")
    public ResponseEntity<List<historicodto.Response>> listar(@PathVariable Long idChamado) {
        return ResponseEntity.ok(historicoService.listarPorChamado(idChamado));
    }
}