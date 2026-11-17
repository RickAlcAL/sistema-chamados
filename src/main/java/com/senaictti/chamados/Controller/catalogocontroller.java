package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.chamadodto;
import com.senaictti.chamados.Service.chamadoservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 Listas de apoio para o formulário de abrir chamado (os ids vão em idGrupo e idPrioridade).
 */
@RestController
public class catalogocontroller {

    private final chamadoservice chamadoService;

    public catalogocontroller(chamadoservice chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping("/grupos")
    public ResponseEntity<List<chamadodto.GrupoResponse>> grupos() {
        return ResponseEntity.ok(chamadoService.listarGrupos());
    }

    @GetMapping("/prioridades")
    public ResponseEntity<List<chamadodto.PrioridadeResponse>> prioridades() {
        return ResponseEntity.ok(chamadoService.listarPrioridades());
    }
}
