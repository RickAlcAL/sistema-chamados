package com.senaictti.chamados.DTO;

import com.senaictti.chamados.Entity.perfilusuario;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public class usuariodto {

    //DTO que irá enviar os dados para o back-end de acordo com a arquitetura
    public record Request(@NotBlank(message = "Nome é obrigatorio")
                          String nomeUsuario,
                          @NotNull(message = "Idade é obrigatorio")
                          @Min(value = 0, message = "Idade inválida")
                          Integer idadeUsuario,
                          @NotBlank(message = "CEP é obrigatorio")
                          @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos")
                          String cepUsuario,
                          @NotBlank(message = "CPF é obrigatorio")
                          @CPF(message = "CPF inválido")
                          String cpfUsuario,
                          @NotBlank(message = "A senha é obrigatoria")
                          @Size(min = 6,message = "Sua senha deve conter no minimo 6 caracteres")
                          String senhaUsuario){}

    //DTO que devolve a resposta pra API (nunca devolve senha nem CPF)
    public record Response(Long id,
                           String nomeUsuario,
                           Integer idadeUsuario,
                           String cepUsuario,
                           perfilusuario perfil,
                           boolean ativo){}

    // PUT /usuarios/{id} - dados que o próprio usuário (ou um ADMIN) pode alterar
    public record AtualizarRequest(@NotBlank(message = "Nome é obrigatorio")
                                   String nomeUsuario,
                                   @NotNull(message = "Idade é obrigatorio")
                                   @Min(value = 0, message = "Idade inválida")
                                   Integer idadeUsuario,
                                   @NotBlank(message = "CEP é obrigatorio")
                                   @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos")
                                   String cepUsuario){}

    // PATCH /usuarios/{id}/perfil - só ADMIN
    public record PerfilRequest(@NotNull(message = "Perfil é obrigatorio")
                                perfilusuario perfil){}

    // PATCH /usuarios/{id}/status - só ADMIN (ativar/desativar usuário)
    public record StatusRequest(@NotNull(message = "O campo ativo é obrigatorio")
                                Boolean ativo){}

    // PUT /auth/senha - troca da própria senha
    public record SenhaRequest(@NotBlank(message = "A senha atual é obrigatoria")
                               String senhaAtual,
                               @NotBlank(message = "A nova senha é obrigatoria")
                               @Size(min = 6, message = "Sua senha deve conter no minimo 6 caracteres")
                               String novaSenha){}
}
