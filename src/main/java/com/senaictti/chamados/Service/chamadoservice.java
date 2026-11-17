package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.chamadodto;
import com.senaictti.chamados.Entity.chamado;
import com.senaictti.chamados.Entity.grupo;
import com.senaictti.chamados.Entity.perfilusuario;
import com.senaictti.chamados.Entity.prioridade;
import com.senaictti.chamados.Entity.statuschamado;
import com.senaictti.chamados.Entity.usuario;
import com.senaictti.chamados.Repository.chamadorepository;
import com.senaictti.chamados.Repository.gruporepository;
import com.senaictti.chamados.Repository.prioridaderepository;
import com.senaictti.chamados.Repository.usuariorepository;
import com.senaictti.chamados.Security.usuariodetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/*
 Núcleo dos chamados: abrir, listar, buscar, editar, assumir e mudar status.
 Todo evento importante também é gravado no histórico (historicoservice).
 Regras que a Yasmin pode refinar depois: prioridade, prazo (SLA), encaminhamento e escalonamento.
 */
@Service
public class chamadoservice {

    private static final String PRIORIDADE_PADRAO = "MEDIA";
    private static final String GRUPO_PADRAO = "N1";

    private final chamadorepository chamadoRepository;
    private final gruporepository grupoRepository;
    private final prioridaderepository prioridadeRepository;
    private final usuariorepository usuarioRepository;
    private final historicoservice historicoService;
    private final solucaoservice solucaoService;

    public chamadoservice(chamadorepository chamadoRepository,
                          gruporepository grupoRepository,
                          prioridaderepository prioridadeRepository,
                          usuariorepository usuarioRepository,
                          historicoservice historicoService,
                          solucaoservice solucaoService) {
        this.chamadoRepository = chamadoRepository;
        this.grupoRepository = grupoRepository;
        this.prioridadeRepository = prioridadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoService = historicoService;
        this.solucaoService = solucaoService;
    }

    // POST /chamados
    @Transactional
    public chamadodto.Response abrir(chamadodto.Request request, usuariodetails logado) {
        usuario solicitante = buscarUsuario(logado.getId());

        prioridade prioridadeEscolhida = request.idPrioridade() != null
                ? prioridadeRepository.findById(request.idPrioridade())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prioridade inválida"))
                : prioridadeRepository.findByNomePrioridade(PRIORIDADE_PADRAO)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Prioridade padrão não cadastrada"));

        grupo grupoEscolhido = request.idGrupo() != null
                ? grupoRepository.findById(request.idGrupo())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grupo inválido"))
                : grupoRepository.findByNomeGrupo(GRUPO_PADRAO)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Grupo padrão não cadastrado"));

        LocalDateTime agora = LocalDateTime.now();
        chamado c = new chamado();
        c.setTitulo(request.titulo());
        c.setDescricao(request.descricao());
        c.setStatus(statuschamado.ABERTO);
        c.setSolicitante(solicitante);
        c.setGrupo(grupoEscolhido);
        c.setPrioridade(prioridadeEscolhida);
        c.setDataAbertura(agora);
        c.setDataPrazo(agora.plusHours(prioridadeEscolhida.getPrazoHoras()));
        chamado salvo = chamadoRepository.save(c);

        historicoService.registrar(salvo.getId(), logado.getId(), "STATUS",
                null, statuschamado.ABERTO.name(), "Chamado aberto: " + salvo.getTitulo());

        return toResponse(salvo);
    }

    // GET /chamados?status=ABERTO
    // Equipe (TECNICO/ADMIN) vê tudo; SOLICITANTE vê só os próprios chamados
    @Transactional(readOnly = true)
    public List<chamadodto.Response> listar(statuschamado status, usuariodetails logado) {
        List<chamado> lista;
        if (ehEquipe(logado)) {
            lista = status == null
                    ? chamadoRepository.findAllByOrderByDataAberturaDesc()
                    : chamadoRepository.findByStatusOrderByDataAberturaDesc(status);
        } else {
            lista = status == null
                    ? chamadoRepository.findBySolicitanteIdOrderByDataAberturaDesc(logado.getId())
                    : chamadoRepository.findBySolicitanteIdAndStatusOrderByDataAberturaDesc(logado.getId(), status);
        }
        return lista.stream().map(this::toResponse).toList();
    }

    // GET /chamados/{id}
    @Transactional(readOnly = true)
    public chamadodto.Response buscar(Long id, usuariodetails logado) {
        chamado c = buscarEntidade(id);
        checarAcesso(c, logado);
        return toResponse(c);
    }

    // PUT /chamados/{id} - dono (só enquanto ABERTO) ou ADMIN. Chamado FECHADO não muda mais
    @Transactional
    public chamadodto.Response atualizar(Long id, chamadodto.AtualizarRequest request, usuariodetails logado) {
        chamado c = buscarEntidade(id);
        checarAcesso(c, logado);
        boolean admin = logado.getPerfil() == perfilusuario.ADMIN;
        boolean dono = c.getSolicitante().getId().equals(logado.getId());
        if (!admin && !dono) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só o autor do chamado ou um ADMIN pode editá-lo");
        }
        if (c.getStatus() == statuschamado.FECHADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chamado fechado não pode ser editado");
        }
        if (!admin && c.getStatus() != statuschamado.ABERTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chamado em atendimento não pode ser editado pelo solicitante");
        }
        c.setTitulo(request.titulo());
        c.setDescricao(request.descricao());
        chamado salvo = chamadoRepository.save(c);

        historicoService.registrar(id, logado.getId(), "COMENTARIO", null, null,
                "Título/descrição do chamado foram alterados");
        return toResponse(salvo);
    }

    // PATCH /chamados/{id}/assumir - o técnico (ou ADMIN) assume um chamado ABERTO
    @Transactional
    public chamadodto.Response assumir(Long id, usuariodetails logado) {
        chamado c = buscarEntidade(id);
        if (c.getStatus() != statuschamado.ABERTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Só é possível assumir um chamado com status ABERTO");
        }
        return mudarStatus(id,
                new chamadodto.StatusRequest(statuschamado.EM_ATENDIMENTO, "Chamado assumido por " + logado.getNome()),
                logado);
    }

    // PATCH /chamados/{id}/status
    @Transactional
    public chamadodto.Response mudarStatus(Long id, chamadodto.StatusRequest request, usuariodetails logado) {
        chamado c = buscarEntidade(id);
        statuschamado atual = c.getStatus();
        statuschamado novo = request.status();

        if (atual == novo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O chamado já está com o status " + atual);
        }
        if (!transicaoPermitida(atual, novo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Transição de status não permitida: " + atual + " -> " + novo);
        }

        LocalDateTime agora = LocalDateTime.now();
        if (novo == statuschamado.FECHADO) {
            // Regra do projeto: só fecha com solução registrada
            if (!solucaoService.existeSolucao(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Registre a solução (POST /solucoes) antes de fechar o chamado");
            }
            c.setDataEncerramento(agora);
            c.setSlaCumprido(!agora.isAfter(c.getDataPrazo()));
        }
        if (novo == statuschamado.EM_ATENDIMENTO && c.getTecnico() == null) {
            c.setTecnico(buscarUsuario(logado.getId()));
        }
        if (novo == statuschamado.ABERTO) {
            c.setTecnico(null); // voltou para a fila
        }
        c.setStatus(novo);
        chamado salvo = chamadoRepository.save(c);

        String descricao = (request.comentario() != null && !request.comentario().isBlank())
                ? request.comentario()
                : "Status alterado de " + atual + " para " + novo;
        historicoService.registrar(id, logado.getId(), "STATUS", atual.name(), novo.name(), descricao);

        return toResponse(salvo);
    }

    // Listas de apoio (selects do front)
    @Transactional(readOnly = true)
    public List<chamadodto.GrupoResponse> listarGrupos() {
        return grupoRepository.findAll().stream()
                .map(g -> new chamadodto.GrupoResponse(g.getId(), g.getNomeGrupo(), g.getTipoGrupo()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<chamadodto.PrioridadeResponse> listarPrioridades() {
        return prioridadeRepository.findAll().stream()
                .map(p -> new chamadodto.PrioridadeResponse(p.getId(), p.getNomePrioridade(), p.getPrazoHoras()))
                .toList();
    }

    // ----- métodos auxiliares -----

    private boolean transicaoPermitida(statuschamado atual, statuschamado novo) {
        return switch (atual) {
            case ABERTO -> novo == statuschamado.EM_ATENDIMENTO || novo == statuschamado.FECHADO;
            case EM_ATENDIMENTO -> novo == statuschamado.ABERTO || novo == statuschamado.FECHADO;
            case FECHADO -> false;
        };
    }

    private boolean ehEquipe(usuariodetails logado) {
        return logado.getPerfil() != perfilusuario.SOLICITANTE;
    }

    // Equipe vê qualquer chamado; SOLICITANTE só os próprios
    private void checarAcesso(chamado c, usuariodetails logado) {
        if (!ehEquipe(logado) && !c.getSolicitante().getId().equals(logado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para acessar este chamado");
        }
    }

    private chamado buscarEntidade(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado não encontrado"));
    }

    private usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }

    private chamadodto.Response toResponse(chamado c) {
        usuario tecnico = c.getTecnico();
        return new chamadodto.Response(
                c.getId(),
                c.getTitulo(),
                c.getDescricao(),
                c.getStatus(),
                c.getSolicitante().getId(),
                c.getSolicitante().getNomeUsuario(),
                tecnico != null ? tecnico.getId() : null,
                tecnico != null ? tecnico.getNomeUsuario() : null,
                c.getGrupo().getId(),
                c.getGrupo().getNomeGrupo(),
                c.getGrupo().getTipoGrupo(),
                c.getPrioridade().getId(),
                c.getPrioridade().getNomePrioridade(),
                c.getDataAbertura(),
                c.getDataPrazo(),
                c.getDataEncerramento(),
                c.getSlaCumprido());
    }
}
