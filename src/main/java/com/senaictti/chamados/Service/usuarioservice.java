package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.usuariodto;
import com.senaictti.chamados.Entity.usuario;
import com.senaictti.chamados.Repository.usuariorepository;
import com.senaictti.chamados.Security.passwordconfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class usuarioservice {

    private final usuariorepository repository;

    private final passwordconfig encoder;
    public usuarioservice(usuariorepository repository, passwordconfig encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    //Cadastro de usuario
    public usuariodto.Response cadastrar(usuariodto.Request request){
        if (repository.existsByCpfUsuario(request.cpfUsuario())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        usuario user = new usuario();
        user.setNomeUsuario(request.nomeUsuario());
        user.setIdadeUsuario(request.idadeUsuario());
        user.setCepUsuario(request.cepUsuario());
        user.setCpfUsuario(request.cpfUsuario());
        //Aplicando a criptografia na senha
        user.setSenhaUsuario(encoder.passwordEncoder().encode(request.senhaUsuario()));
        usuario userSave = repository.save(user);
        return new usuariodto.Response(userSave.getId(),
                userSave.getNomeUsuario(),
                userSave.getIdadeUsuario(),
                userSave.getCepUsuario());
    }
}
