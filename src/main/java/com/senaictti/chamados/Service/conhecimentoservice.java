package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.conhecimentodto;
import com.senaictti.chamados.Entity.baseconhecimento;
import com.senaictti.chamados.Repository.conhecimentorepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class conhecimentoservice {

    private final conhecimentorepository conhecimentoRepository;

    public conhecimentoservice(conhecimentorepository conhecimentoRepository) {
        this.conhecimentoRepository = conhecimentoRepository;
    }

    // Cadastra um novo artigo na base de conhecimento
    public conhecimentodto.Response cadastrar(conhecimentodto.Request r) {
        baseconhecimento c = new baseconhecimento();
        c.setTitulo(r.titulo());
        c.setDescricaoProblema(r.descricaoProblema());
        c.setDescricaoSolucao(r.descricaoSolucao());
        c.setIdChamado(r.idChamado());
        c.setIdUsuario(r.idUsuario());
        c.setDataCriacao(LocalDateTime.now());
        return paraResponse(conhecimentoRepository.save(c));
    }

    // Busca por palavra; se o termo vier vazio, lista todos os artigos
    public List<conhecimentodto.Response> buscar(String termo) {
        List<baseconhecimento> lista = (termo == null || termo.isBlank())
                ? conhecimentoRepository.findAll()
                : conhecimentoRepository.buscar(termo.trim());
        return lista.stream().map(this::paraResponse).toList();
    }

    public conhecimentodto.Response buscarPorId(Long id) {
        return conhecimentoRepository.findById(id)
                .map(this::paraResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artigo não encontrado"));
    }

    // Artigos ligados a um chamado ajuda em problema recorrente
    public List<conhecimentodto.Response> listarPorChamado(Long idChamado) {
        return conhecimentoRepository.findByIdChamado(idChamado).stream().map(this::paraResponse).toList();
    }

    private conhecimentodto.Response paraResponse(baseconhecimento c) {
        return new conhecimentodto.Response(c.getId(), c.getTitulo(), c.getDescricaoProblema(),
                c.getDescricaoSolucao(), c.getIdChamado(), c.getIdUsuario(), c.getDataCriacao());
    }
}