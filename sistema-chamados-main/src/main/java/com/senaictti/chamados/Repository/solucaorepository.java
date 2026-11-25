package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.solucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface solucaorepository extends JpaRepository<solucao, Long> {
    // Usado para saber se o chamado já tem solução
    boolean existsByIdChamado(Long idChamado);
    Optional<solucao> findByIdChamado(Long idChamado);
}