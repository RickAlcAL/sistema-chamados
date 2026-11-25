package com.senaictti.chamados.Security;

import com.senaictti.chamados.Entity.perfilusuario;
import com.senaictti.chamados.Entity.usuario;
import com.senaictti.chamados.Repository.usuariorepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*
 Como o cadastro público só cria SOLICITANTE, alguém precisa nascer ADMIN para
 promover os outros. Ao subir a aplicação, se NÃO existir nenhum ADMIN, este
 componente cria um com os dados do application.properties (app.admin.*).
 */
@Component
public class adminseeder implements CommandLineRunner {

    private final usuariorepository repository;
    private final PasswordEncoder encoder;

    @Value("${app.admin.nome:Administrador}")
    private String nome;
    @Value("${app.admin.cpf:52998224725}")
    private String cpf;
    @Value("${app.admin.senha:admin123}")
    private String senha;

    public adminseeder(usuariorepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repository.existsByPerfil(perfilusuario.ADMIN)) {
            return;
        }
        if (repository.existsByCpfUsuario(cpf)) {
            System.out.println("[adminseeder] CPF do admin padrão já existe com outro perfil. Promova-o direto no banco.");
            return;
        }
        usuario admin = new usuario();
        admin.setNomeUsuario(nome);
        admin.setIdadeUsuario(18);
        admin.setCepUsuario("00000000");
        admin.setCpfUsuario(cpf);
        admin.setSenhaUsuario(encoder.encode(senha));
        admin.setPerfil(perfilusuario.ADMIN);
        admin.setAtivo(true);
        repository.save(admin);
        System.out.println("[adminseeder] ADMIN padrão criado. CPF: " + cpf + " (troque a senha depois do primeiro login!)");
    }
}
