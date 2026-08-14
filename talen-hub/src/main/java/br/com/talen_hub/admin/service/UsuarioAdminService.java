package br.com.talen_hub.admin.service;

import br.com.talen_hub.admin.dto.UsuarioAdminResponse;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;

import java.util.List;

public interface UsuarioAdminService {

    List<UsuarioAdminResponse> listarUsuarios(
            String categoria,
            String skill
    );

    UsuarioAdminResponse buscarUsuarioPorId(
            Long usuarioId
    );

    List<UsuarioSkillResponse> listarSkillsUsuario(
            Long usuarioId
    );
}