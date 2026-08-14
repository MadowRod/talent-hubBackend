package br.com.talen_hub.usuarioSkill.service;

import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.ResourceNotFoundException;
import br.com.talen_hub.skill.entity.Skill;
import br.com.talen_hub.skill.repository.SkillRepository;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import br.com.talen_hub.usuarioSkill.dto.request.AtualizarLevelRequest;
import br.com.talen_hub.usuarioSkill.dto.request.UsuarioSkillRequest;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;
import br.com.talen_hub.usuarioSkill.entity.UsuarioSkill;
import br.com.talen_hub.usuarioSkill.repository.UsuarioSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioSkillServiceImpl implements UsuarioSkillService {

    private final UsuarioSkillRepository usuarioSkillRepository;
    private final UsuarioRepository usuarioRepository;
    private final SkillRepository skillRepository;

    public UsuarioSkillServiceImpl(
            UsuarioSkillRepository usuarioSkillRepository,
            UsuarioRepository usuarioRepository,
            SkillRepository skillRepository
    ) {
        this.usuarioSkillRepository = usuarioSkillRepository;
        this.usuarioRepository = usuarioRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioSkillResponse> listarPorUsuario(Long usuarioId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return usuarioSkillRepository
                .findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioSkillResponse associar(
            Long usuarioId,
            UsuarioSkillRequest request
    ) {

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Usuário não encontrado")
                );

        Skill skill = skillRepository
                .findById(request.skillId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill não encontrada")
                );

        boolean existe =
                usuarioSkillRepository
                        .existsByUsuarioIdAndSkillId(
                                usuarioId,
                                request.skillId()
                        );

        if (existe) {
            throw new BusinessException("Usuário já possui essa skill");
        }

        UsuarioSkill usuarioSkill = UsuarioSkill.builder()
                .usuario(usuario)
                .skill(skill)
                .level(request.level())
                .build();

        UsuarioSkill salva =
                usuarioSkillRepository.save(usuarioSkill);

        return toResponse(salva);
    }

    @Override
    @Transactional
    public UsuarioSkillResponse atualizarLevel(
            Long usuarioId,
            Long associacaoId,
            AtualizarLevelRequest request
    ) {

        UsuarioSkill usuarioSkill =
                usuarioSkillRepository
                        .findByIdAndUsuarioId(
                                associacaoId,
                                usuarioId
                        )
                        .orElseThrow(() ->
                            new ResourceNotFoundException("Associação de skill não encontrada"));

        usuarioSkill.setLevel(request.level());

        UsuarioSkill atualizada =
                usuarioSkillRepository.save(usuarioSkill);

        return toResponse(atualizada);
    }

    @Override
    @Transactional
    public void excluir(
            Long usuarioId,
            Long associacaoId
    ) {

        UsuarioSkill usuarioSkill =
                usuarioSkillRepository
                        .findByIdAndUsuarioId(
                                associacaoId,
                                usuarioId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Associação de skill não encontrada"
                                )
                        );

        usuarioSkillRepository.delete(usuarioSkill);
    }

    private UsuarioSkillResponse toResponse(
            UsuarioSkill usuarioSkill
    ) {

        Skill skill = usuarioSkill.getSkill();

        return new UsuarioSkillResponse(
                usuarioSkill.getId(),
                usuarioSkill.getUsuario().getId(),
                usuarioSkill.getUsuario().getNome(),
                skill.getId(),
                skill.getNome(),
                skill.getDescricao(),
                skill.getImagemUrl(),
                skill.getCategoria().getId(),
                skill.getCategoria().getNome(),
                usuarioSkill.getLevel()
        );
    }
}