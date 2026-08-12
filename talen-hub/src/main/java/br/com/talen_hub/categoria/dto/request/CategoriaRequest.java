package br.com.talen_hub.categoria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(

        @NotBlank(message = "Nome da categoria é obrigatório")
        @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
        String nome
) {
}