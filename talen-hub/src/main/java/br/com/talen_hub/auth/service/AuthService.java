package br.com.talen_hub.auth.service;

import br.com.talen_hub.auth.dto.request.CadastroRequest;
import br.com.talen_hub.auth.dto.request.LoginRequest;
import br.com.talen_hub.auth.dto.response.LoginResponse;
import br.com.talen_hub.auth.dto.response.UsuarioResponse;
import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.jwt.service.JwtService;
import br.com.talen_hub.shared.enums.Role;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioResponse cadastrar(CadastroRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException(
                    "Já existe um usuário cadastrado com este email"
            );
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(Role.USER)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return UsuarioResponse.fromEntity(usuarioSalvo);
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Email ou senha inválidos"
                        )
                );

        if (!usuario.getAtivo()) {
            throw new BadCredentialsException(
                    "Usuário está inativo"
            );
        }

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenha()
        )) {
            throw new BadCredentialsException(
                    "Email ou senha inválidos"
            );
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer"
        );
    }
}