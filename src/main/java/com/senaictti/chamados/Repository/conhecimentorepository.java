package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.baseconhecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface conhecimentorepository extends JpaRepository<baseconhecimento, Long> {

    // Busca a palavra no título, no problema ou na solução
    @Query("SELECT c FROM baseconhecimento c WHERE " +
            "LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(c.descricaoProblema) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(c.descricaoSolucao) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "ORDER BY c.dataCriacao DESC")
    List<baseconhecimento> buscar(@Param("termo") String termo);

    // ligados a um chamado específico
    List<baseconhecimento> findByIdChamado(Long idChamado);
}