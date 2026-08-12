package br.com.talen_hub.usuarioskill.service;

import br.com.talen_hub.categoria.entity.Categoria;
import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.ResourceNotFoundException;
import br.com.talen_hub.shared.enums.Level;
import br.com.talen_hub.skill.entity.Skill;
import br.com.talen_hub.skill.repository.SkillRepository;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import br.com.talen_hub.usuarioSkill.dto.request.AtualizarLevelRequest;
import br.com.talen_hub.usuarioSkill.dto.request.UsuarioSkillRequest;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;
import br.com.talen_hub.usuarioSkill.entity.UsuarioSkill;
import br.com.talen_hub.usuarioSkill.repository.UsuarioSkillRepository;
import br.com.talen_hub.usuarioSkill.service.UsuarioSkillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSkillServiceImplTest {

    @Mock
    private UsuarioSkillRepository usuarioSkillRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private UsuarioSkillServiceImpl service;

    private Usuario usuario;
    private Categoria categoria;
    private Skill skill;
    private UsuarioSkill usuarioSkill;

    @BeforeEach
    void setUp() {

        categoria = Categoria.builder()
                .id(1L)
                .nome("Programação")
                .build();

        skill = Skill.builder()
                .id(10L)
                .nome("Java")
                .categoria(categoria)
                .build();

        usuario = Usuario.builder()
                .id(100L)
                .nome("Rodrigo")
                .email("rodrigo@email.com")
                .build();

        usuarioSkill = UsuarioSkill.builder()
                .id(1000L)
                .usuario(usuario)
                .skill(skill)
                .level(Level.BASICO)
                .build();
    }

    @Test
    void deveListarSkillsDoUsuario() {

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));

        when(usuarioSkillRepository.findByUsuarioId(100L))
                .thenReturn(List.of(usuarioSkill));

        List<UsuarioSkillResponse> resultado =
                service.listarPorUsuario(100L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        UsuarioSkillResponse response = resultado.get(0);

        assertEquals(1000L, response.id());
        assertEquals(100L, response.usuarioId());
        assertEquals("Rodrigo", response.usuarioNome());
        assertEquals(10L, response.skillId());
        assertEquals("Java", response.skillNome());
        assertEquals(1L, response.categoriaId());
        assertEquals("Programação", response.categoriaNome());
        assertEquals(Level.BASICO, response.level());

        verify(usuarioRepository).findById(100L);
        verify(usuarioSkillRepository).findByUsuarioId(100L);
    }

    @Test
    void deveRetornarListaVaziaQuandoUsuarioNaoPossuiSkills() {

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));

        when(usuarioSkillRepository.findByUsuarioId(100L))
                .thenReturn(List.of());

        List<UsuarioSkillResponse> resultado =
                service.listarPorUsuario(100L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioSkillRepository).findByUsuarioId(100L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExisteAoListar() {

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.listarPorUsuario(100L)
        );

        verify(usuarioSkillRepository, never())
                .findByUsuarioId(any());
    }

    @Test
    void deveAssociarSkillAoUsuario() {

        UsuarioSkillRequest request =
                new UsuarioSkillRequest(
                        10L,
                        Level.INTERMEDIARIO
                );

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));

        when(skillRepository.findById(10L))
                .thenReturn(Optional.of(skill));

        when(usuarioSkillRepository
                .existsByUsuarioIdAndSkillId(100L, 10L))
                .thenReturn(false);

        when(usuarioSkillRepository.save(any(UsuarioSkill.class)))
                .thenAnswer(invocation -> {
                    UsuarioSkill entidade =
                            invocation.getArgument(0);

                    entidade.setId(1000L);

                    return entidade;
                });

        UsuarioSkillResponse resultado =
                service.associar(100L, request);

        assertNotNull(resultado);
        assertEquals(1000L, resultado.id());
        assertEquals(100L, resultado.usuarioId());
        assertEquals(10L, resultado.skillId());
        assertEquals(Level.INTERMEDIARIO, resultado.level());

        ArgumentCaptor<UsuarioSkill> captor =
                ArgumentCaptor.forClass(UsuarioSkill.class);

        verify(usuarioSkillRepository)
                .save(captor.capture());

        UsuarioSkill salva = captor.getValue();

        assertEquals(usuario, salva.getUsuario());
        assertEquals(skill, salva.getSkill());
        assertEquals(Level.INTERMEDIARIO, salva.getLevel());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExisteAoAssociar() {

        UsuarioSkillRequest request =
                new UsuarioSkillRequest(
                        10L,
                        Level.BASICO
                );

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.associar(100L, request)
        );

        verifyNoInteractions(skillRepository);
        verify(usuarioSkillRepository, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoQuandoSkillNaoExisteAoAssociar() {

        UsuarioSkillRequest request =
                new UsuarioSkillRequest(
                        10L,
                        Level.BASICO
                );

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));

        when(skillRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.associar(100L, request)
        );

        verify(usuarioSkillRepository, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioJaPossuiSkill() {

        UsuarioSkillRequest request =
                new UsuarioSkillRequest(
                        10L,
                        Level.BASICO
                );

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));

        when(skillRepository.findById(10L))
                .thenReturn(Optional.of(skill));

        when(usuarioSkillRepository
                .existsByUsuarioIdAndSkillId(100L, 10L))
                .thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> service.associar(100L, request)
        );

        verify(usuarioSkillRepository, never())
                .save(any());
    }

    @Test
    void deveAtualizarLevelDaSkill() {

        AtualizarLevelRequest request =
                new AtualizarLevelRequest(
                        Level.AVANCADO
                );

        when(usuarioSkillRepository
                .findByIdAndUsuarioId(1000L, 100L))
                .thenReturn(Optional.of(usuarioSkill));

        when(usuarioSkillRepository.save(usuarioSkill))
                .thenReturn(usuarioSkill);

        UsuarioSkillResponse resultado =
                service.atualizarLevel(
                        100L,
                        1000L,
                        request
                );

        assertNotNull(resultado);
        assertEquals(Level.AVANCADO, resultado.level());
        assertEquals(Level.AVANCADO, usuarioSkill.getLevel());

        verify(usuarioSkillRepository)
                .findByIdAndUsuarioId(1000L, 100L);

        verify(usuarioSkillRepository)
                .save(usuarioSkill);
    }

    @Test
    void deveLancarExcecaoQuandoAssociacaoNaoExisteAoAtualizarLevel() {

        AtualizarLevelRequest request =
                new AtualizarLevelRequest(
                        Level.AVANCADO
                );

        when(usuarioSkillRepository
                .findByIdAndUsuarioId(1000L, 100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.atualizarLevel(
                        100L,
                        1000L,
                        request
                )
        );

        verify(usuarioSkillRepository, never())
                .save(any());
    }

    @Test
    void deveExcluirAssociacao() {

        when(usuarioSkillRepository
                .findByIdAndUsuarioId(1000L, 100L))
                .thenReturn(Optional.of(usuarioSkill));

        service.excluir(100L, 1000L);

        verify(usuarioSkillRepository)
                .findByIdAndUsuarioId(1000L, 100L);

        verify(usuarioSkillRepository)
                .delete(usuarioSkill);
    }

    @Test
    void deveLancarExcecaoQuandoAssociacaoNaoExisteAoExcluir() {

        when(usuarioSkillRepository
                .findByIdAndUsuarioId(1000L, 100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.excluir(100L, 1000L)
        );

        verify(usuarioSkillRepository, never())
                .delete(any());
    }
}