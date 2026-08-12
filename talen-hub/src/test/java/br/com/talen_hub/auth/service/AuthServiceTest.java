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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private CadastroRequest cadastroRequest;
    private LoginRequest loginRequest;
    private Usuario usuario;

    @BeforeEach
    void setUp() {

        cadastroRequest = new CadastroRequest(
                "Rodrigo",
                "rodrigo@email.com",
                "123456"
        );

        loginRequest = new LoginRequest(
                "rodrigo@email.com",
                "123456"
        );

        usuario = Usuario.builder()
                .id(1L)
                .nome("Rodrigo")
                .email("rodrigo@email.com")
                .senha("senha-criptografada")
                .role(Role.USER)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {

        when(usuarioRepository.existsByEmail(cadastroRequest.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(cadastroRequest.senha()))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        UsuarioResponse response =
                authService.cadastrar(cadastroRequest);

        assertNotNull(response);
        assertEquals(usuario.getId(), response.id());
        assertEquals(usuario.getNome(), response.nome());
        assertEquals(usuario.getEmail(), response.email());
        assertEquals(Role.USER, response.role());
        assertTrue(response.ativo());

        verify(usuarioRepository)
                .existsByEmail(cadastroRequest.email());

        verify(passwordEncoder)
                .encode(cadastroRequest.senha());

        verify(usuarioRepository)
                .save(any(Usuario.class));
    }

    @Test
    void deveCadastrarUsuarioComRoleUser() {

        when(usuarioRepository.existsByEmail(cadastroRequest.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(cadastroRequest.senha()))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        authService.cadastrar(cadastroRequest);

        ArgumentCaptor<Usuario> captor =
                ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioSalvo = captor.getValue();

        assertEquals(Role.USER, usuarioSalvo.getRole());
        assertTrue(usuarioSalvo.getAtivo());
        assertEquals(
                "senha-criptografada",
                usuarioSalvo.getSenha()
        );
    }

    @Test
    void deveCriptografarSenhaAntesDeSalvar() {

        when(usuarioRepository.existsByEmail(cadastroRequest.email()))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        authService.cadastrar(cadastroRequest);

        verify(passwordEncoder)
                .encode("123456");

        ArgumentCaptor<Usuario> captor =
                ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioRepository).save(captor.capture());

        assertEquals(
                "senha-criptografada",
                captor.getValue().getSenha()
        );
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailDuplicado() {

        when(usuarioRepository.existsByEmail(cadastroRequest.email()))
                .thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> authService.cadastrar(cadastroRequest)
                );

        assertEquals(
                "Já existe um usuário cadastrado com este email",
                exception.getMessage()
        );

        verify(usuarioRepository)
                .existsByEmail(cadastroRequest.email());

        verify(usuarioRepository, never())
                .save(any(Usuario.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void deveRealizarLoginComSucesso() {

        when(usuarioRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                loginRequest.senha(),
                usuario.getSenha()
        )).thenReturn(true);

        when(jwtService.gerarToken(usuario))
                .thenReturn("token-jwt");

        LoginResponse response =
                authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("token-jwt", response.token());
        assertEquals("Bearer", response.tipo());

        verify(usuarioRepository)
                .findByEmail(loginRequest.email());

        verify(passwordEncoder)
                .matches(
                        loginRequest.senha(),
                        usuario.getSenha()
                );

        verify(jwtService)
                .gerarToken(usuario);
    }

    @Test
    void naoDeveRealizarLoginComEmailInexistente() {

        when(usuarioRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.empty());

        BadCredentialsException exception =
                assertThrows(
                        BadCredentialsException.class,
                        () -> authService.login(loginRequest)
                );

        assertEquals(
                "Email ou senha inválidos",
                exception.getMessage()
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .gerarToken(any(Usuario.class));
    }

    @Test
    void naoDeveRealizarLoginComSenhaIncorreta() {

        when(usuarioRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                loginRequest.senha(),
                usuario.getSenha()
        )).thenReturn(false);

        BadCredentialsException exception =
                assertThrows(
                        BadCredentialsException.class,
                        () -> authService.login(loginRequest)
                );

        assertEquals(
                "Email ou senha inválidos",
                exception.getMessage()
        );

        verify(passwordEncoder)
                .matches(
                        loginRequest.senha(),
                        usuario.getSenha()
                );

        verify(jwtService, never())
                .gerarToken(any(Usuario.class));
    }

    @Test
    void naoDeveRealizarLoginComUsuarioInativo() {

        usuario.setAtivo(false);

        when(usuarioRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.of(usuario));

        BadCredentialsException exception =
                assertThrows(
                        BadCredentialsException.class,
                        () -> authService.login(loginRequest)
                );

        assertEquals(
                "Usuário está inativo",
                exception.getMessage()
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .gerarToken(any(Usuario.class));
    }

    @Test
    void deveGerarTokenSomenteDepoisDeValidarSenha() {

        when(usuarioRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                loginRequest.senha(),
                usuario.getSenha()
        )).thenReturn(true);

        when(jwtService.gerarToken(usuario))
                .thenReturn("meu-token");

        LoginResponse response =
                authService.login(loginRequest);

        assertEquals("meu-token", response.token());

        verify(passwordEncoder).matches(
                loginRequest.senha(),
                usuario.getSenha()
        );

        verify(jwtService).gerarToken(usuario);
    }

    @Test
    void devePreencherDataDeCriacaoAoCadastrar() {

        when(usuarioRepository.existsByEmail(cadastroRequest.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(cadastroRequest.senha()))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        authService.cadastrar(cadastroRequest);

        ArgumentCaptor<Usuario> captor =
                ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioRepository).save(captor.capture());

        assertNotNull(captor.getValue().getDataCriacao());
    }
}