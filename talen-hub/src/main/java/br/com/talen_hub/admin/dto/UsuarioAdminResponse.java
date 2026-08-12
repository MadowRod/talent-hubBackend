package br.com.talen_hub.admin.dto;

import br.com.talen_hub.shared.enums.Role;

public record UsuarioAdminResponse(
        Long id,
        String nome,
        String email,
        Role role,
        Boolean ativo
) {
}