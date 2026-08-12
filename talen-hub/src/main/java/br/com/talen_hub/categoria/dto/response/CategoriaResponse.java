package br.com.talen_hub.categoria.dto.response;

import br.com.talen_hub.categoria.entity.Categoria;

public record CategoriaResponse(
        Long id,
        String nome
) {

    public static CategoriaResponse fromEntity(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }
}