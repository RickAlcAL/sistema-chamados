package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.solucaodto;
import com.senaictti.chamados.Entity.solucao;
import com.senaictti.chamados.Repository.solucaorepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class solucaoservice {

    private final solucaorepository solucaoRepository;
    private final historicoservice historicoService;

    public solucaoservice(solucaorepository solucaoRepository, historicoservice historicoService) {
        this.solucaoRepository = solucaoRepository;
        this.historicoService = historicoService;
    }

    // Registra a solução do chamado só uma por chamado
    public solucaodto.Response registrar(solucaodto.Request r) {
        if (solucaoRepository.existsByIdChamado(r.idChamado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este chamado já possui solução registrada");
        }
        solucao s = new solucao();
        s.setIdChamado(r.idChamado());
        s.setIdUsuario(r.idUsuario());
        s.setDescricaoSolucao(r.descricaoSolucao());
        s.setDataRegistro(LocalDateTime.now());
        solucao salva = solucaoRepository.save(s);

        // Toda solução registrada também vira um evento no histórico do chamado
        historicoService.registrar(r.idChamado(), r.idUsuario(), "SOLUCAO", null, null,
                "Solução registrada: " + r.descricaoSolucao());

        return paraResponse(salva);
    }

    // Busca a solução de um chamado 404 se ainda não existir
    public solucaodto.Response buscarPorChamado(Long idChamado) {
        return solucaoRepository.findByIdChamado(idChamado)
                .map(this::paraResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhuma solução registrada para este chamado"));
    }

    /*
     Regra do projeto: o chamado só fecha com solução registrada.
     if (!solucaoService.existeSolucao(idChamado)) { ...bloquear o fechamento... }
     */
    public boolean existeSolucao(Long idChamado) {
        return solucaoRepository.existsByIdChamado(idChamado);
    }

    private solucaodto.Response paraResponse(solucao s) {
        return new solucaodto.Response(s.getId(), s.getIdChamado(), s.getIdUsuario(),
                s.getDescricaoSolucao(), s.getDataRegistro());
    }
}