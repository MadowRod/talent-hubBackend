package br.com.talen_hub.admin.service;

import br.com.talen_hub.admin.dto.UsuarioAdminResponse;
import br.com.talen_hub.exception.ResourceNotFoundException;
import br.com.talen_hub.shared.enums.Level;
import br.com.talen_hub.shared.enums.Role;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;
import br.com.talen_hub.usuarioSkill.repository.UsuarioSkillRepository;
import br.com.talen_hub.usuarioSkill.service.UsuarioSkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAdminServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioSkillRepository usuarioSkillRepository;

    @Mock
    private UsuarioSkillService usuarioSkillService;

    @InjectMocks
    private UsuarioAdminServiceImpl service;

    private Usuario usuario;

    private Usuario usuario2;

    @BeforeEach
    void setUp() {

        usuario = Usuario.builder()
                .id(1L)
                .nome("Rodrigo")
                .email("rodrigo@email.com")
                .role(Role.USER)
                .ativo(true)
                .build();

        usuario2 = Usuario.builder()
                .id(2L)
                .nome("Maria")
                .email("maria@email.com")
                .role(Role.ADMIN)
                .ativo(true)
                .build();
    }

    @Test
    void deveListarTodosOsUsuarios() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario, usuario2));

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(null, null);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals(1L, resultado.get(0).id());
        assertEquals("Rodrigo", resultado.get(0).nome());
        assertEquals("rodrigo@email.com", resultado.get(0).email());
        assertEquals(Role.USER, resultado.get(0).role());
        assertTrue(resultado.get(0).ativo());

        assertEquals(2L, resultado.get(1).id());
        assertEquals("Maria", resultado.get(1).nome());

        verify(usuarioRepository).findAll();
        verify(usuarioSkillRepository, never())
                .findUsuarioIdsByCategoria(anyString());
        verify(usuarioSkillRepository, never())
                .findUsuarioIdsBySkill(anyString());
    }

    @Test
    void deveListarListaVaziaQuandoNaoExistemUsuarios() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of());

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(null, null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioRepository).findAll();
    }

    @Test
    void deveFiltrarUsuariosPorCategoria() {

        List<Long> ids = List.of(1L);

        when(usuarioSkillRepository
                .findUsuarioIdsByCategoria("Programação"))
                .thenReturn(ids);

        when(usuarioRepository.findAllById(ids))
                .thenReturn(List.of(usuario));

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(
                        "Programação",
                        null
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        assertEquals(1L, resultado.get(0).id());
        assertEquals("Rodrigo", resultado.get(0).nome());

        verify(usuarioSkillRepository)
                .findUsuarioIdsByCategoria("Programação");

        verify(usuarioRepository)
                .findAllById(ids);

        verify(usuarioRepository, never()).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoCategoriaNaoPossuiUsuarios() {

        List<Long> ids = List.of();

        when(usuarioSkillRepository
                .findUsuarioIdsByCategoria("Programação"))
                .thenReturn(ids);

        when(usuarioRepository.findAllById(ids))
                .thenReturn(List.of());

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(
                        "Programação",
                        null
                );

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioSkillRepository)
                .findUsuarioIdsByCategoria("Programação");

        verify(usuarioRepository)
                .findAllById(ids);
    }

    @Test
    void deveFiltrarUsuariosPorSkill() {

        List<Long> ids = List.of(1L);

        when(usuarioSkillRepository
                .findUsuarioIdsBySkill("Java"))
                .thenReturn(ids);

        when(usuarioRepository.findAllById(ids))
                .thenReturn(List.of(usuario));

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(
                        null,
                        "Java"
                );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        assertEquals(1L, resultado.get(0).id());
        assertEquals("Rodrigo", resultado.get(0).nome());

        verify(usuarioSkillRepository)
                .findUsuarioIdsBySkill("Java");

        verify(usuarioRepository)
                .findAllById(ids);

        verify(usuarioRepository, never()).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoSkillNaoPossuiUsuarios() {

        List<Long> ids = List.of();

        when(usuarioSkillRepository
                .findUsuarioIdsBySkill("Java"))
                .thenReturn(ids);

        when(usuarioRepository.findAllById(ids))
                .thenReturn(List.of());

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(
                        null,
                        "Java"
                );

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioSkillRepository)
                .findUsuarioIdsBySkill("Java");

        verify(usuarioRepository)
                .findAllById(ids);
    }

    @Test
    void deveIgnorarCategoriaQuandoEstiverEmBranco() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios("   ", null);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id());

        verify(usuarioRepository).findAll();

        verify(usuarioSkillRepository, never())
                .findUsuarioIdsByCategoria(anyString());
    }

    @Test
    void deveIgnorarSkillQuandoEstiverEmBranco() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(null, "   ");

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id());

        verify(usuarioRepository).findAll();

        verify(usuarioSkillRepository, never())
                .findUsuarioIdsBySkill(anyString());
    }

    @Test
    void devePriorizarCategoriaQuandoCategoriaESkillForemInformadas() {

        List<Long> ids = List.of(1L);

        when(usuarioSkillRepository
                .findUsuarioIdsByCategoria("Programação"))
                .thenReturn(ids);

        when(usuarioRepository.findAllById(ids))
                .thenReturn(List.of(usuario));

        List<UsuarioAdminResponse> resultado =
                service.listarUsuarios(
                        "Programação",
                        "Java"
                );

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id());

        verify(usuarioSkillRepository)
                .findUsuarioIdsByCategoria("Programação");

        verify(usuarioSkillRepository, never())
                .findUsuarioIdsBySkill(anyString());
    }

    @Test
    void deveListarSkillsDoUsuario() {

        UsuarioSkillResponse skillResponse =
                new UsuarioSkillResponse(
                        100L,
                        1L,
                        "Rodrigo",
                        10L,
                        "Java",
                        1L,
                        "Programação",
                        Level.AVANCADO
                );

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioSkillService.listarPorUsuario(1L))
                .thenReturn(List.of(skillResponse));

        List<UsuarioSkillResponse> resultado =
                service.listarSkillsUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        assertEquals(100L, resultado.get(0).id());
        assertEquals(1L, resultado.get(0).usuarioId());
        assertEquals("Rodrigo", resultado.get(0).usuarioNome());
        assertEquals(10L, resultado.get(0).skillId());
        assertEquals("Java", resultado.get(0).skillNome());
        assertEquals(Level.AVANCADO, resultado.get(0).level());

        verify(usuarioRepository).findById(1L);

        verify(usuarioSkillService)
                .listarPorUsuario(1L);
    }

    @Test
    void deveRetornarListaVaziaQuandoUsuarioNaoPossuiSkills() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioSkillService.listarPorUsuario(1L))
                .thenReturn(List.of());

        List<UsuarioSkillResponse> resultado =
                service.listarSkillsUsuario(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioSkillService)
                .listarPorUsuario(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExisteAoListarSkills() {

        when(usuarioRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.listarSkillsUsuario(999L)
        );

        verify(usuarioSkillService, never())
                .listarPorUsuario(anyLong());
    }
}