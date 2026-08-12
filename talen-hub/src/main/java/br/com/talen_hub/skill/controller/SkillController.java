package br.com.talen_hub.skill.controller;

import br.com.talen_hub.skill.dto.request.SkillRequest;
import br.com.talen_hub.skill.dto.response.SkillResponse;
import br.com.talen_hub.skill.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> criar(
            @Valid @RequestBody SkillRequest request
    ) {

        SkillResponse response = skillService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SkillResponse>> listar() {

        return ResponseEntity.ok(
                skillService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                skillService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequest request
    ) {

        return ResponseEntity.ok(
                skillService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {

        skillService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}