package br.com.talen_hub.admin.service;

import br.com.talen_hub.admin.dto.UsuarioAdminResponse;
import br.com.talen_hub.exception.ResourceNotFoundException;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;
import br.com.talen_hub.usuarioSkill.repository.UsuarioSkillRepository;
import br.com.talen_hub.usuarioSkill.service.UsuarioSkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioAdminServiceImpl
        implements UsuarioAdminService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioSkillRepository usuarioSkillRepository;
    private final UsuarioSkillService usuarioSkillService;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioAdminResponse> listarUsuarios(
            String categoria,
            String skill
    ) {

        List<Usuario> usuarios;

        if (categoria != null && !categoria.isBlank()) {

            List<Long> ids =
                    usuarioSkillRepository
                            .findUsuarioIdsByCategoria(categoria);

            usuarios = usuarioRepository.findAllById(ids);

        } else if (skill != null && !skill.isBlank()) {

            List<Long> ids =
                    usuarioSkillRepository
                            .findUsuarioIdsBySkill(skill);

            usuarios = usuarioRepository.findAllById(ids);

        } else {

            usuarios = usuarioRepository.findAll();
        }

        return usuarios.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioAdminResponse buscarUsuarioPorId(
            Long usuarioId
    ) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado"
                        )
                );

        return toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioSkillResponse> listarSkillsUsuario(
            Long usuarioId
    ) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado"
                        )
                );

        return usuarioSkillService
                .listarPorUsuario(usuarioId);
    }

    private UsuarioAdminResponse toResponse(
            Usuario usuario
    ) {

        return new UsuarioAdminResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getAtivo()
        );
    }
}