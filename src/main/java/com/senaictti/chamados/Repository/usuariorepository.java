package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usuariorepository extends JpaRepository<usuario, Long> {
    boolean existsByCpfUsuario(String cpfUsuario);
}
