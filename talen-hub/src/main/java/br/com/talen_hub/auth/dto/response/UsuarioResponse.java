package br.com.talen_hub.auth.dto.response;

import br.com.talen_hub.shared.enums.Role;
import br.com.talen_hub.usuario.entity.Usuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Role role,
        Boolean ativo
) {

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getAtivo()
        );
    }
}