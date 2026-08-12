package br.com.talen_hub.skill.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SkillRequest(

        @NotBlank(message = "O nome da skill é obrigatório")
        @Size(max = 100, message = "O nome da skill deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId

) {
}