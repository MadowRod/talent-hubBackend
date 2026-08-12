package br.com.talen_hub.config;

import br.com.talen_hub.shared.enums.Role;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner criarUsuarioMaster(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            String emailMaster = "master@talenthub.com";

            if (usuarioRepository.existsByEmail(emailMaster)) {
                return;
            }

            Usuario master = Usuario.builder()
                    .nome("Administrador")
                    .email(emailMaster)
                    .senha(passwordEncoder.encode("Master@123"))
                    .role(Role.ADMIN)
                    .ativo(true)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            usuarioRepository.save(master);

            System.out.println(
                    "Usuário MASTER criado: " + emailMaster
            );
        };
    }
}