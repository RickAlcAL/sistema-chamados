package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.indicadordto;
import com.senaictti.chamados.Service.indicadorservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 Endpoints dos indicadores . O front end usa estes
 endpoints no dashboard.
 */
@RestController
@RequestMapping("/indicadores")
public class indicadorcontroller {

    private final indicadorservice indicadorService;

    public indicadorcontroller(indicadorservice indicadorService) {
        this.indicadorService = indicadorService;
    }

    // Chamados produtivos x administrativos: quantidade e tempo médio
    @GetMapping("/por-tipo")
    public ResponseEntity<List<indicadordto.Item>> porTipo() {
        return ResponseEntity.ok(indicadorService.porTipo());
    }

    // Por prioridade
    @GetMapping("/por-prioridade")
    public ResponseEntity<List<indicadordto.Item>> porPrioridade() {
        return ResponseEntity.ok(indicadorService.porPrioridade());
    }

    // Por grupo (N1, N2, N3...)
    @GetMapping("/por-grupo")
    public ResponseEntity<List<indicadordto.Item>> porGrupo() {
        return ResponseEntity.ok(indicadorService.porGrupo());
    }

    // SLA cumprido x estourado
    @GetMapping("/sla")
    public ResponseEntity<indicadordto.Sla> sla() {
        return ResponseEntity.ok(indicadorService.sla());
    }
}