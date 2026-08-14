package br.com.talen_hub.skill.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SkillRequest(

        @NotBlank(message = "O nome da skill é obrigatório")
        @Size(max = 100, message = "O nome da skill deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "A descrição da skill é obrigatória")
        @Size(max = 500, message = "A descrição da skill deve ter no máximo 500 caracteres")
        String descricao,

        @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres")
        String imagemUrl,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId

) {
}