package com.senaictti.chamados.Controller;

import com.senaictti.chamados.DTO.usuariodto;
import com.senaictti.chamados.Service.usuarioservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cadastro")
public class usuariocontroller {


    //Iniciando service
    private final usuarioservice usuarioService;
    //Consturtor para atribuir um valor para o service
    public usuariocontroller(usuarioservice usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Adicionando usuario cadastrado
    @PostMapping
    public ResponseEntity<usuariodto.Response> cadastrar(@RequestBody @Valid usuariodto.Request request) {
        //Retornando a entidade um corpo com atributos e request
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(request));
    }
}
