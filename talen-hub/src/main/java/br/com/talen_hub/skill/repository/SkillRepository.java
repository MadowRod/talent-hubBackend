package br.com.talen_hub.skill.repository;

import br.com.talen_hub.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository
        extends JpaRepository<Skill, Long> {

    List<Skill> findByCategoriaId(Long categoriaId);

    Optional<Skill> findByNomeAndCategoriaId(
            String nome,
            Long categoriaId
    );

    boolean existsByNomeAndCategoriaId(
            String nome,
            Long categoriaId
    );

    boolean existsByNomeAndCategoriaIdAndIdNot(
            String nome,
            Long categoriaId,
            Long id
    );
}