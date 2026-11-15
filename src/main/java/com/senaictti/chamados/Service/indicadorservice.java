package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.indicadordto;
import com.senaictti.chamados.Repository.indicadorrepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class indicadorservice {

    private final indicadorrepository indicadorRepository;

    public indicadorservice(indicadorrepository indicadorRepository) {
        this.indicadorRepository = indicadorRepository;
    }

    public List<indicadordto.Item> porTipo() { return indicadorRepository.porTipo(); }
    public List<indicadordto.Item> porPrioridade() { return indicadorRepository.porPrioridade(); }
    public List<indicadordto.Item> porGrupo() { return indicadorRepository.porGrupo(); }
    public indicadordto.Sla sla() { return indicadorRepository.sla(); }
}