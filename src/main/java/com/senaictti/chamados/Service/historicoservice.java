package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.historicodto;
import com.senaictti.chamados.Entity.historicochamado;
import com.senaictti.chamados.Repository.historicorepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class historicoservice {

    private final historicorepository historicoRepository;

    public historicoservice(historicorepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }


    public historicodto.Response registrar(Long idChamado, Long idUsuario, String tipoEvento,
                                           String statusAnterior, String statusNovo, String descricao) {
        historicochamado h = new historicochamado();
        h.setIdChamado(idChamado);
        h.setIdUsuario(idUsuario);
        h.setTipoEvento(tipoEvento);
        h.setStatusAnterior(statusAnterior);
        h.setStatusNovo(statusNovo);
        h.setDescricao(descricao);
        h.setDataRegistro(LocalDateTime.now()); // data e hora gravadas automaticamente
        return paraResponse(historicoRepository.save(h));
    }

    // Versão usada pelo controller recebe o DTO inteiro
    public historicodto.Response registrar(historicodto.Request r) {
        return registrar(r.idChamado(), r.idUsuario(), r.tipoEvento(),
                r.statusAnterior(), r.statusNovo(), r.descricao());
    }

    // Histórico completo de um chamado, em ordem cronológica
    public List<historicodto.Response> listarPorChamado(Long idChamado) {
        return historicoRepository.findByIdChamadoOrderByDataRegistroAsc(idChamado)
                .stream().map(this::paraResponse).toList();
    }

    // Converte a entidade em DTO de resposta
    private historicodto.Response paraResponse(historicochamado h) {
        return new historicodto.Response(h.getId(), h.getIdChamado(), h.getIdUsuario(),
                h.getTipoEvento(), h.getStatusAnterior(), h.getStatusNovo(),
                h.getDescricao(), h.getDataRegistro());
    }
}