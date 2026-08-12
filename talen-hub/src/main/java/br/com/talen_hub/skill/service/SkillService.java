package br.com.talen_hub.skill.service;

import br.com.talen_hub.skill.dto.request.SkillRequest;
import br.com.talen_hub.skill.dto.response.SkillResponse;

import java.util.List;

public interface SkillService {

    SkillResponse criar(SkillRequest request);

    List<SkillResponse> listar();

    SkillResponse buscarPorId(Long id);

    SkillResponse atualizar(Long id, SkillRequest request);

    void excluir(Long id);
}