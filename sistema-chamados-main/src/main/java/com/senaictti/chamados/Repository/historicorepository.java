package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.historicochamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface historicorepository extends JpaRepository<historicochamado, Long> {
    // Devolve o histórico do chamado do evento mais antigo para o mais novo
    List<historicochamado> findByIdChamadoOrderByDataRegistroAsc(Long idChamado);
}