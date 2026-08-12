package br.com.talen_hub.usuarioSkill.dto.response;

import br.com.talen_hub.shared.enums.Level;

public record UsuarioSkillResponse(

        Long id,

        Long usuarioId,

        String usuarioNome,

        Long skillId,

        String skillNome,

        Long categoriaId,

        String categoriaNome,

        Level level

) {
}