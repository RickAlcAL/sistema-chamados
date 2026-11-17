package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.prioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface prioridaderepository extends JpaRepository<prioridade, Long> {
    Optional<prioridade> findByNomePrioridade(String nomePrioridade);
}
