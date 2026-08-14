package br.com.talen_hub.skill.dto.response;

public record SkillResponse(

        Long id,
        String nome,
        String descricao,
        String imagemUrl,
        Long categoriaId,
        String categoriaNome

) {
}