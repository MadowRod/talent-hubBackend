package br.com.talen_hub.categoria.service;

import br.com.talen_hub.categoria.dto.request.CategoriaRequest;
import br.com.talen_hub.categoria.dto.response.CategoriaResponse;
import br.com.talen_hub.categoria.entity.Categoria;
import br.com.talen_hub.categoria.repository.CategoriaRepository;
import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaResponse criar(CategoriaRequest request) {

        if (categoriaRepository.existsByNome(request.nome())) {
            throw new BusinessException(
                    "Já existe uma categoria com este nome"
            );
        }

        Categoria categoria = Categoria.builder()
                .nome(request.nome())
                .build();

        Categoria salva = categoriaRepository.save(categoria);

        return CategoriaResponse.fromEntity(salva);
    }

    public List<CategoriaResponse> listar() {

        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponse::fromEntity)
                .toList();
    }

    public CategoriaResponse buscarPorId(Long id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoria não encontrada"
                        )
                );

        return CategoriaResponse.fromEntity(categoria);
    }

    public CategoriaResponse atualizar(
            Long id,
            CategoriaRequest request
    ) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoria não encontrada"
                        )
                );

        if (!categoria.getNome().equals(request.nome())
                && categoriaRepository.existsByNome(request.nome())) {

            throw new BusinessException(
                    "Já existe uma categoria com este nome"
            );
        }

        categoria.setNome(request.nome());

        Categoria atualizada = categoriaRepository.save(categoria);

        return CategoriaResponse.fromEntity(atualizada);
    }

    public void excluir(Long id) {

        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Categoria não encontrada"
            );
        }

        categoriaRepository.deleteById(id);
    }
}