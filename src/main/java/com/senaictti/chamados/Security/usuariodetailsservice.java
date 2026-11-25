package com.senaictti.chamados.Security;

import com.senaictti.chamados.Repository.usuariorepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 O Spring Security chama esta classe no login para buscar o usuário pelo CPF
 e depois compara a senha digitada com o hash BCrypt do banco.
 */
@Service
public class usuariodetailsservice implements UserDetailsService {

    private final usuariorepository repository;

    public usuariodetailsservice(usuariorepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        // Aceita CPF com ou sem pontuação (529.982.247-25 ou 52998224725)
        String somenteNumeros = cpf == null ? "" : cpf.replaceAll("\\D", "");
        return repository.findByCpfUsuario(somenteNumeros)
                .map(usuariodetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
