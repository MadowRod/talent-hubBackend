package br.com.talen_hub.auth.dto.response;

public record LoginResponse(
        String token,
        String tipo
) {
}