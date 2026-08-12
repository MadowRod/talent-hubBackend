package br.com.talen_hub.categoria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "categorias",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categoria_nome",
                        columnNames = "nome"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;
}