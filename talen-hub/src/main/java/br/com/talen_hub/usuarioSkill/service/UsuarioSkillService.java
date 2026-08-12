package br.com.talen_hub.usuarioSkill.service;

import br.com.talen_hub.usuarioSkill.dto.request.AtualizarLevelRequest;
import br.com.talen_hub.usuarioSkill.dto.request.UsuarioSkillRequest;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;

import java.util.List;

public interface UsuarioSkillService {

    List<UsuarioSkillResponse> listarPorUsuario(Long usuarioId);

    UsuarioSkillResponse associar(
            Long usuarioId,
            UsuarioSkillRequest request
    );

    UsuarioSkillResponse atualizarLevel(
            Long usuarioId,
            Long associacaoId,
            AtualizarLevelRequest request
    );

    void excluir(
            Long usuarioId,
            Long associacaoId
    );
}