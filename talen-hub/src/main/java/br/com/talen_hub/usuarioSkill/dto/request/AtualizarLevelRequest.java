package br.com.talen_hub.usuarioSkill.dto.request;

import br.com.talen_hub.shared.enums.Level;
import jakarta.validation.constraints.NotNull;

public record AtualizarLevelRequest(

        @NotNull(message = "O nível é obrigatório")
        Level level

) {
}