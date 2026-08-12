package br.com.talen_hub.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 150, message = "Email deve possuir no máximo 150 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve possuir entre 6 e 100 caracteres")
        String senha
) {
}