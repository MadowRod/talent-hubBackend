package br.com.talen_hub.skill.entity;

import br.com.talen_hub.categoria.entity.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_skill_nome_categoria",
                        columnNames = {"nome", "categoria_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "skill_generator"
    )
    @SequenceGenerator(
            name = "skill_generator",
            sequenceName = "skills_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "categoria_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_skill_categoria"
            )
    )
    private Categoria categoria;
}