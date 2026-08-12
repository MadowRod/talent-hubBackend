package br.com.talen_hub.skill.service;

import br.com.talen_hub.categoria.entity.Categoria;
import br.com.talen_hub.categoria.repository.CategoriaRepository;
import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.ResourceNotFoundException;
import br.com.talen_hub.skill.dto.request.SkillRequest;
import br.com.talen_hub.skill.dto.response.SkillResponse;
import br.com.talen_hub.skill.entity.Skill;
import br.com.talen_hub.skill.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final CategoriaRepository categoriaRepository;

    public SkillServiceImpl(
            SkillRepository skillRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.skillRepository = skillRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public SkillResponse criar(SkillRequest request) {

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada")
                );

        boolean existe = skillRepository
                .existsByNomeAndCategoriaId(
                        request.nome(),
                        request.categoriaId()
                );

        if (existe) {
            throw new BusinessException(
                    "Já existe uma skill com esse nome nesta categoria"
            );
        }

        Skill skill = Skill.builder()
                .nome(request.nome())
                .categoria(categoria)
                .build();

        Skill salva = skillRepository.save(skill);

        return toResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillResponse> listar() {

        return skillRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SkillResponse buscarPorId(Long id) {

        Skill skill = skillRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill não encontrada")
                );

        return toResponse(skill);
    }

    @Override
    @Transactional
    public SkillResponse atualizar(
            Long id,
            SkillRequest request
    ) {

        Skill skill = skillRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill não encontrada")
                );

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada")
                );

        boolean existe = skillRepository
                .existsByNomeAndCategoriaIdAndIdNot(
                        request.nome(),
                        request.categoriaId(),
                        id
                );

        if (existe) {
            throw new BusinessException(
                    "Já existe uma skill com esse nome nesta categoria"
            );
        }

        skill.setNome(request.nome());
        skill.setCategoria(categoria);

        Skill atualizada = skillRepository.save(skill);

        return toResponse(atualizada);
    }

    @Override
    @Transactional
    public void excluir(Long id) {

        Skill skill = skillRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill não encontrada")
                );

        skillRepository.delete(skill);
    }

    private SkillResponse toResponse(Skill skill) {

        return new SkillResponse(
                skill.getId(),
                skill.getNome(),
                skill.getCategoria().getId(),
                skill.getCategoria().getNome()
        );
    }
}