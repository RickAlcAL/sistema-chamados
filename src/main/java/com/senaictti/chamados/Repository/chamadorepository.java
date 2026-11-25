package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.chamado;
import com.senaictti.chamados.Entity.statuschamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface chamadorepository extends JpaRepository<chamado, Long> {

    // Visão da equipe (TECNICO/ADMIN): todos os chamados, mais novos primeiro
    List<chamado> findAllByOrderByDataAberturaDesc();
    List<chamado> findByStatusOrderByDataAberturaDesc(statuschamado status);

    // Visão do SOLICITANTE: só os chamados que ele abriu
    List<chamado> findBySolicitanteIdOrderByDataAberturaDesc(Long idSolicitante);
    List<chamado> findBySolicitanteIdAndStatusOrderByDataAberturaDesc(Long idSolicitante, statuschamado status);
}
