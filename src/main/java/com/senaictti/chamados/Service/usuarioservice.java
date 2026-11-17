package com.senaictti.chamados.Service;

import com.senaictti.chamados.DTO.usuariodto;
import com.senaictti.chamados.Entity.perfilusuario;
import com.senaictti.chamados.Entity.usuario;
import com.senaictti.chamados.Repository.usuariorepository;
import com.senaictti.chamados.Security.passwordconfig;
import com.senaictti.chamados.Security.usuariodetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class usuarioservice {

    private final usuariorepository repository;

    private final passwordconfig encoder;
    public usuarioservice(usuariorepository repository, passwordconfig encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    //Cadastro de usuario (público). Sempre nasce como SOLICITANTE: só um ADMIN muda o perfil depois
    public usuariodto.Response cadastrar(usuariodto.Request request){
        if (repository.existsByCpfUsuario(request.cpfUsuario())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        usuario user = new usuario();
        user.setNomeUsuario(request.nomeUsuario());
        user.setIdadeUsuario(request.idadeUsuario());
        user.setCepUsuario(request.cepUsuario());
        user.setCpfUsuario(request.cpfUsuario());
        user.setPerfil(perfilusuario.SOLICITANTE);
        user.setAtivo(true);
        //Aplicando a criptografia na senha
        user.setSenhaUsuario(encoder.passwordEncoder().encode(request.senhaUsuario()));
        usuario userSave = repository.save(user);
        return toResponse(userSave);
    }

    // GET /auth/me - dados de quem está logado
    @Transactional(readOnly = true)
    public usuariodto.Response me(usuariodetails logado) {
        return toResponse(buscarEntidade(logado.getId()));
    }

    // GET /usuarios - só ADMIN (garantido no controller)
    @Transactional(readOnly = true)
    public List<usuariodto.Response> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    // GET /usuarios/{id} - o próprio usuário ou ADMIN
    @Transactional(readOnly = true)
    public usuariodto.Response buscar(Long id, usuariodetails logado) {
        checarAcesso(id, logado);
        return toResponse(buscarEntidade(id));
    }

    // PUT /usuarios/{id} - o próprio usuário ou ADMIN
    @Transactional
    public usuariodto.Response atualizar(Long id, usuariodto.AtualizarRequest request, usuariodetails logado) {
        checarAcesso(id, logado);
        usuario user = buscarEntidade(id);
        user.setNomeUsuario(request.nomeUsuario());
        user.setIdadeUsuario(request.idadeUsuario());
        user.setCepUsuario(request.cepUsuario());
        return toResponse(repository.save(user));
    }

    // PATCH /usuarios/{id}/perfil - só ADMIN
    @Transactional
    public usuariodto.Response alterarPerfil(Long id, usuariodto.PerfilRequest request, usuariodetails logado) {
        // Evita o ADMIN tirar o próprio acesso e deixar o sistema sem administrador
        if (id.equals(logado.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode alterar o seu próprio perfil");
        }
        usuario user = buscarEntidade(id);
        user.setPerfil(request.perfil());
        return toResponse(repository.save(user));
    }

    // PATCH /usuarios/{id}/status - só ADMIN
    @Transactional
    public usuariodto.Response alterarStatus(Long id, usuariodto.StatusRequest request, usuariodetails logado) {
        if (id.equals(logado.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode desativar a sua própria conta");
        }
        usuario user = buscarEntidade(id);
        user.setAtivo(request.ativo());
        return toResponse(repository.save(user));
    }

    // PUT /auth/senha - troca a própria senha
    @Transactional
    public void alterarSenha(usuariodetails logado, usuariodto.SenhaRequest request) {
        usuario user = buscarEntidade(logado.getId());
        if (!encoder.passwordEncoder().matches(request.senhaAtual(), user.getSenhaUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }
        user.setSenhaUsuario(encoder.passwordEncoder().encode(request.novaSenha()));
        repository.save(user);
    }

    // ----- métodos auxiliares -----

    private usuario buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    // ADMIN acessa qualquer usuário; os demais só acessam a si mesmos
    private void checarAcesso(Long id, usuariodetails logado) {
        boolean admin = logado.getPerfil() == perfilusuario.ADMIN;
        if (!admin && !logado.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para acessar este usuário");
        }
    }

    private usuariodto.Response toResponse(usuario u) {
        return new usuariodto.Response(u.getId(),
                u.getNomeUsuario(),
                u.getIdadeUsuario(),
                u.getCepUsuario(),
                u.getPerfil(),
                u.isAtivo());
    }
}
