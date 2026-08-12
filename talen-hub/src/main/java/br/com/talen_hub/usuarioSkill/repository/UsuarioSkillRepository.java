package br.com.talen_hub.usuarioSkill.repository;

import br.com.talen_hub.usuarioSkill.entity.UsuarioSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioSkillRepository
        extends JpaRepository<UsuarioSkill, Long> {

    List<UsuarioSkill> findByUsuarioId(Long usuarioId);

    Optional<UsuarioSkill> findByIdAndUsuarioId(
            Long id,
            Long usuarioId
    );

    boolean existsByUsuarioIdAndSkillId(
            Long usuarioId,
            Long skillId
    );

    @Query("""
            SELECT DISTINCT us.usuario.id
            FROM UsuarioSkill us
            JOIN us.skill s
            JOIN s.categoria c
            WHERE LOWER(c.nome) = LOWER(:categoria)
            """)
    List<Long> findUsuarioIdsByCategoria(
            @Param("categoria") String categoria
    );

    @Query("""
            SELECT DISTINCT us.usuario.id
            FROM UsuarioSkill us
            JOIN us.skill s
            WHERE LOWER(s.nome) = LOWER(:skill)
            """)
    List<Long> findUsuarioIdsBySkill(
            @Param("skill") String skill
    );
}