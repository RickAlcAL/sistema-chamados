package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.conhecimentodto;
import com.senaictti.chamados.Service.conhecimentoservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conhecimento")
public class conhecimentocontroller {

    private final conhecimentoservice conhecimentoService;

    public conhecimentocontroller(conhecimentoservice conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    // POST /conhecimento - cadastra um artigo
    @PostMapping
    public ResponseEntity<conhecimentodto.Response> cadastrar(@RequestBody @Valid conhecimentodto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conhecimentoService.cadastrar(request));
    }

    // GET /conhecimento?busca=erp - busca por palavra sem "busca" lista tudo
    @GetMapping
    public ResponseEntity<List<conhecimentodto.Response>> buscar(@RequestParam(required = false) String busca) {
        return ResponseEntity.ok(conhecimentoService.buscar(busca));
    }

    // GET /conhecimento/3 - um artigo específico
    @GetMapping("/{id}")
    public ResponseEntity<conhecimentodto.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(conhecimentoService.buscarPorId(id));
    }

    // GET /conhecimento/chamado/5 - artigos ligados ao chamado 5
    @GetMapping("/chamado/{idChamado}")
    public ResponseEntity<List<conhecimentodto.Response>> listarPorChamado(@PathVariable Long idChamado) {
        return ResponseEntity.ok(conhecimentoService.listarPorChamado(idChamado));
    }
}