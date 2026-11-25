package com.senaictti.chamados.Repository;

import com.senaictti.chamados.Entity.perfilusuario;
import com.senaictti.chamados.Entity.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface usuariorepository extends JpaRepository<usuario, Long> {
    boolean existsByCpfUsuario(String cpfUsuario);

    // Usado no login (o CPF é o "username")
    Optional<usuario> findByCpfUsuario(String cpfUsuario);

    // Usado para criar o primeiro ADMIN
    boolean existsByPerfil(perfilusuario perfil);
}
