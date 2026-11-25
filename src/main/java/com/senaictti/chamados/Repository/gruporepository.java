package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface gruporepository extends JpaRepository<grupo, Long> {
    Optional<grupo> findByNomeGrupo(String nomeGrupo);
}
