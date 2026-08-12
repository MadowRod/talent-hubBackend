package br.com.talen_hub.skill.service;

import br.com.talen_hub.categoria.entity.Categoria;
import br.com.talen_hub.categoria.repository.CategoriaRepository;
import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.ResourceNotFoundException;
import br.com.talen_hub.skill.dto.request.SkillRequest;
import br.com.talen_hub.skill.dto.response.SkillResponse;
import br.com.talen_hub.skill.entity.Skill;
import br.com.talen_hub.skill.repository.SkillRepository;
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
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private SkillServiceImpl skillService;

    private Categoria categoria;

    private Skill skill;

    private SkillRequest request;

    @BeforeEach
    void setUp() {

        categoria = Categoria.builder()
                .id(1L)
                .nome("Tecnologia")
                .build();

        skill = Skill.builder()
                .id(1L)
                .nome("Java")
                .categoria(categoria)
                .build();

        request = new SkillRequest(
                "Java",
                1L
        );
    }

    @Test
    void deveCriarSkillComSucesso() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(skillRepository.existsByNomeAndCategoriaId(
                "Java",
                1L
        )).thenReturn(false);

        when(skillRepository.save(any(Skill.class)))
                .thenReturn(skill);

        SkillResponse response =
                skillService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Java", response.nome());
        assertEquals(1L, response.categoriaId());
        assertEquals("Tecnologia", response.categoriaNome());

        verify(categoriaRepository).findById(1L);

        verify(skillRepository)
                .existsByNomeAndCategoriaId(
                        "Java",
                        1L
                );

        verify(skillRepository)
                .save(any(Skill.class));
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoExistirAoCriar() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> skillService.criar(request)
                );

        assertEquals(
                "Categoria não encontrada",
                exception.getMessage()
        );

        verify(categoriaRepository).findById(1L);

        verifyNoInteractions(skillRepository);
    }

    @Test
    void deveLancarExcecaoQuandoSkillJaExistirNaCategoria() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(skillRepository.existsByNomeAndCategoriaId(
                "Java",
                1L
        )).thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> skillService.criar(request)
                );

        assertEquals(
                "Já existe uma skill com esse nome nesta categoria",
                exception.getMessage()
        );

        verify(skillRepository)
                .existsByNomeAndCategoriaId(
                        "Java",
                        1L
                );

        verify(skillRepository, never())
                .save(any(Skill.class));
    }

    @Test
    void deveListarSkillsComSucesso() {

        Skill skill2 = Skill.builder()
                .id(2L)
                .nome("Spring Boot")
                .categoria(categoria)
                .build();

        when(skillRepository.findAll())
                .thenReturn(List.of(skill, skill2));

        List<SkillResponse> response =
                skillService.listar();

        assertNotNull(response);

        assertEquals(2, response.size());

        assertEquals(
                "Java",
                response.get(0).nome()
        );

        assertEquals(
                "Spring Boot",
                response.get(1).nome()
        );

        verify(skillRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremSkills() {

        when(skillRepository.findAll())
                .thenReturn(List.of());

        List<SkillResponse> response =
                skillService.listar();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(skillRepository).findAll();
    }

    @Test
    void deveBuscarSkillPorIdComSucesso() {

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        SkillResponse response =
                skillService.buscarPorId(1L);

        assertNotNull(response);

        assertEquals(1L, response.id());
        assertEquals("Java", response.nome());
        assertEquals(1L, response.categoriaId());
        assertEquals(
                "Tecnologia",
                response.categoriaNome()
        );

        verify(skillRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSkillNaoExistirAoBuscar() {

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> skillService.buscarPorId(99L)
                );

        assertEquals(
                "Skill não encontrada",
                exception.getMessage()
        );

        verify(skillRepository).findById(99L);
    }

    @Test
    void deveAtualizarSkillComSucesso() {

        SkillRequest updateRequest =
                new SkillRequest(
                        "Java Avançado",
                        1L
                );

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(skillRepository.existsByNomeAndCategoriaIdAndIdNot(
                "Java Avançado",
                1L,
                1L
        )).thenReturn(false);

        when(skillRepository.save(skill))
                .thenReturn(skill);

        SkillResponse response =
                skillService.atualizar(
                        1L,
                        updateRequest
                );

        assertNotNull(response);

        assertEquals(
                "Java Avançado",
                skill.getNome()
        );

        assertEquals(
                "Java Avançado",
                response.nome()
        );

        verify(skillRepository).findById(1L);

        verify(categoriaRepository).findById(1L);

        verify(skillRepository)
                .existsByNomeAndCategoriaIdAndIdNot(
                        "Java Avançado",
                        1L,
                        1L
                );

        verify(skillRepository).save(skill);
    }

    @Test
    void deveLancarExcecaoAoAtualizarSkillInexistente() {

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> skillService.atualizar(
                                99L,
                                request
                        )
                );

        assertEquals(
                "Skill não encontrada",
                exception.getMessage()
        );

        verify(skillRepository).findById(99L);

        verifyNoInteractions(categoriaRepository);
    }

    @Test
    void deveLancarExcecaoAoAtualizarComCategoriaInexistente() {

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        when(categoriaRepository.findById(99L))
                .thenReturn(Optional.empty());

        SkillRequest updateRequest =
                new SkillRequest(
                        "Java",
                        99L
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> skillService.atualizar(
                                1L,
                                updateRequest
                        )
                );

        assertEquals(
                "Categoria não encontrada",
                exception.getMessage()
        );

        verify(skillRepository).findById(1L);

        verify(categoriaRepository)
                .findById(99L);

        verify(skillRepository, never())
                .save(any(Skill.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarComNomeDuplicado() {

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(skillRepository.existsByNomeAndCategoriaIdAndIdNot(
                "Java",
                1L,
                1L
        )).thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> skillService.atualizar(
                                1L,
                                request
                        )
                );

        assertEquals(
                "Já existe uma skill com esse nome nesta categoria",
                exception.getMessage()
        );

        verify(skillRepository, never())
                .save(any(Skill.class));
    }

    @Test
    void deveExcluirSkillComSucesso() {

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        skillService.excluir(1L);

        verify(skillRepository).findById(1L);

        verify(skillRepository).delete(skill);
    }

    @Test
    void deveLancarExcecaoAoExcluirSkillInexistente() {

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> skillService.excluir(99L)
                );

        assertEquals(
                "Skill não encontrada",
                exception.getMessage()
        );

        verify(skillRepository).findById(99L);

        verify(skillRepository, never())
                .delete(any(Skill.class));
    }
}