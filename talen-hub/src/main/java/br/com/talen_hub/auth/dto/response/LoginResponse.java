package br.com.talen_hub.auth.dto.response;

import br.com.talen_hub.shared.enums.Role;

public record LoginResponse(
        String token,
        String tipo,
        Role role
) {
}