package br.com.talen_hub.categoria.service;

import br.com.talen_hub.categoria.dto.request.CategoriaRequest;
import br.com.talen_hub.categoria.dto.response.CategoriaResponse;
import br.com.talen_hub.categoria.entity.Categoria;
import br.com.talen_hub.categoria.repository.CategoriaRepository;
import br.com.talen_hub.categoria.service.CategoriaService;
import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.ResourceNotFoundException;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaRequest request;

    @BeforeEach
    void setUp() {

        categoria = Categoria.builder()
                .id(1L)
                .nome("Tecnologia")
                .build();

        request = new CategoriaRequest(
                "Tecnologia"
        );
    }

    @Test
    void deveCriarCategoriaComSucesso() {

        when(categoriaRepository.existsByNome(request.nome()))
                .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoria);

        CategoriaResponse response =
                categoriaService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Tecnologia", response.nome());

        verify(categoriaRepository)
                .existsByNome(request.nome());

        verify(categoriaRepository)
                .save(any(Categoria.class));
    }

    @Test
    void deveLancarBusinessExceptionAoCriarCategoriaDuplicada() {

        when(categoriaRepository.existsByNome(request.nome()))
                .thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> categoriaService.criar(request)
                );

        assertEquals(
                "Já existe uma categoria com este nome",
                exception.getMessage()
        );

        verify(categoriaRepository)
                .existsByNome(request.nome());

        verify(categoriaRepository, never())
                .save(any(Categoria.class));
    }

    @Test
    void deveListarCategorias() {

        Categoria segundaCategoria = Categoria.builder()
                .id(2L)
                .nome("Programação")
                .build();

        when(categoriaRepository.findAll())
                .thenReturn(List.of(
                        categoria,
                        segundaCategoria
                ));

        List<CategoriaResponse> response =
                categoriaService.listar();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(
                "Tecnologia",
                response.get(0).nome()
        );

        assertEquals(
                "Programação",
                response.get(1).nome()
        );

        verify(categoriaRepository)
                .findAll();
    }

    @Test
    void deveBuscarCategoriaPorId() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        CategoriaResponse response =
                categoriaService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Tecnologia", response.nome());

        verify(categoriaRepository)
                .findById(1L);
    }

    @Test
    void deveLancarResourceNotFoundExceptionAoBuscarCategoriaInexistente() {

        when(categoriaRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> categoriaService.buscarPorId(999L)
                );

        assertEquals(
                "Categoria não encontrada",
                exception.getMessage()
        );

        verify(categoriaRepository)
                .findById(999L);
    }

    @Test
    void deveAtualizarCategoriaComSucesso() {

        CategoriaRequest novoRequest =
                new CategoriaRequest("Desenvolvimento");

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNome("Desenvolvimento"))
                .thenReturn(false);

        when(categoriaRepository.save(categoria))
                .thenReturn(categoria);

        CategoriaResponse response =
                categoriaService.atualizar(
                        1L,
                        novoRequest
                );

        assertNotNull(response);
        assertEquals(
                "Desenvolvimento",
                response.nome()
        );

        assertEquals(
                "Desenvolvimento",
                categoria.getNome()
        );

        verify(categoriaRepository)
                .findById(1L);

        verify(categoriaRepository)
                .existsByNome("Desenvolvimento");

        verify(categoriaRepository)
                .save(categoria);
    }

    @Test
    void deveLancarResourceNotFoundExceptionAoAtualizarCategoriaInexistente() {

        when(categoriaRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> categoriaService.atualizar(
                                999L,
                                request
                        )
                );

        assertEquals(
                "Categoria não encontrada",
                exception.getMessage()
        );

        verify(categoriaRepository)
                .findById(999L);

        verify(categoriaRepository, never())
                .save(any(Categoria.class));
    }

    @Test
    void deveLancarBusinessExceptionAoAtualizarParaNomeDuplicado() {

        CategoriaRequest novoRequest =
                new CategoriaRequest("Programação");

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNome("Programação"))
                .thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> categoriaService.atualizar(
                                1L,
                                novoRequest
                        )
                );

        assertEquals(
                "Já existe uma categoria com este nome",
                exception.getMessage()
        );

        verify(categoriaRepository)
                .findById(1L);

        verify(categoriaRepository)
                .existsByNome("Programação");

        verify(categoriaRepository, never())
                .save(any(Categoria.class));
    }

    @Test
    void deveAtualizarCategoriaMantendoMesmoNome() {

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(categoria))
                .thenReturn(categoria);

        CategoriaResponse response =
                categoriaService.atualizar(
                        1L,
                        request
                );

        assertNotNull(response);
        assertEquals(
                "Tecnologia",
                response.nome()
        );

        verify(categoriaRepository)
                .findById(1L);

        verify(categoriaRepository, never())
                .existsByNome(anyString());

        verify(categoriaRepository)
                .save(categoria);
    }

    @Test
    void deveExcluirCategoriaComSucesso() {

        when(categoriaRepository.existsById(1L))
                .thenReturn(true);

        categoriaService.excluir(1L);

        verify(categoriaRepository)
                .existsById(1L);

        verify(categoriaRepository)
                .deleteById(1L);
    }

    @Test
    void deveLancarResourceNotFoundExceptionAoExcluirCategoriaInexistente() {

        when(categoriaRepository.existsById(999L))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> categoriaService.excluir(999L)
                );

        assertEquals(
                "Categoria não encontrada",
                exception.getMessage()
        );

        verify(categoriaRepository)
                .existsById(999L);

        verify(categoriaRepository, never())
                .deleteById(anyLong());
    }
}