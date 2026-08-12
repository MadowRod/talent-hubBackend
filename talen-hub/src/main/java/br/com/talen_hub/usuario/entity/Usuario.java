package br.com.talen_hub.usuario.entity;

import br.com.talen_hub.shared.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", uniqueConstraints = { @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 150)
    private String nome;

    @Column (nullable = false, length = 150)
    private String email;

    @Column (nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;
}
