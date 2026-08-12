package br.com.talen_hub.usuarioSkill.dto.request;

import br.com.talen_hub.shared.enums.Level;
import jakarta.validation.constraints.NotNull;

public record UsuarioSkillRequest(

        @NotNull(message = "A skill é obrigatória")
        Long skillId,

        @NotNull(message = "O nível é obrigatório")
        Level level

) {
}