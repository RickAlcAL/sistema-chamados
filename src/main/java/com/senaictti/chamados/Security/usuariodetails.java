package com.senaictti.chamados.Security;

import com.senaictti.chamados.Entity.perfilusuario;
import com.senaictti.chamados.Entity.usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
 Representa o usuário logado para o Spring Security.
 Os outros módulos (chamados, histórico...) podem receber ele no controller com:
   @AuthenticationPrincipal usuariodetails logado
 e usar logado.getId() / logado.getPerfil() em vez de pedir o id do usuário no body.
 */
public class usuariodetails implements UserDetails {

    private final Long id;
    private final String nome;
    private final String cpf;
    private final String senha;
    private final perfilusuario perfil;
    private final boolean ativo;

    public usuariodetails(usuario u) {
        this.id = u.getId();
        this.nome = u.getNomeUsuario();
        this.cpf = u.getCpfUsuario();
        this.senha = u.getSenhaUsuario();
        this.perfil = u.getPerfil();
        this.ativo = u.isAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public perfilusuario getPerfil() { return perfil; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // O prefixo ROLE_ é o que faz o hasRole('ADMIN') funcionar
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public String getPassword() { return senha; }

    // O "login" do sistema é o CPF
    @Override
    public String getUsername() { return cpf; }

    @Override
    public boolean isEnabled() { return ativo; }
}
