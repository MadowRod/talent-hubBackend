package br.com.talen_hub.usuarioSkill.entity;

import br.com.talen_hub.shared.enums.Level;
import br.com.talen_hub.skill.entity.Skill;
import br.com.talen_hub.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "usuarios_skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_skill",
                        columnNames = {
                                "usuario_id",
                                "skill_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSkill {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usuario_skill_generator"
    )
    @SequenceGenerator(
            name = "usuario_skill_generator",
            sequenceName = "usuarios_skills_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "usuario_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_usuario_skill_usuario"
            )
    )
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "skill_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_usuario_skill_skill"
            )
    )
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Level level;
}