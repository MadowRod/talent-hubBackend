package br.com.talen_hub.skill.dto.response;

public record SkillResponse(

        Long id,
        String nome,
        Long categoriaId,
        String categoriaNome

) {
}