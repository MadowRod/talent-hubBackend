package br.com.talen_hub.usuarioSkill.controller;

import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuarioSkill.dto.request.AtualizarLevelRequest;
import br.com.talen_hub.usuarioSkill.dto.request.UsuarioSkillRequest;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;
import br.com.talen_hub.usuarioSkill.service.UsuarioSkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario/skills")
public class UsuarioSkillController {

    private final UsuarioSkillService usuarioSkillService;

    public UsuarioSkillController(
            UsuarioSkillService usuarioSkillService
    ) {
        this.usuarioSkillService = usuarioSkillService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioSkillResponse>> listarMinhasSkills(
            Authentication authentication
    ) {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(
                usuarioSkillService.listarPorUsuario(
                        usuario.getId()
                )
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioSkillResponse> associar(
            Authentication authentication,
            @Valid @RequestBody UsuarioSkillRequest request
    ) {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        UsuarioSkillResponse response =
                usuarioSkillService.associar(
                        usuario.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{associacaoId}")
    public ResponseEntity<UsuarioSkillResponse> atualizarLevel(
            Authentication authentication,
            @PathVariable Long associacaoId,
            @Valid @RequestBody AtualizarLevelRequest request
    ) {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(
                usuarioSkillService.atualizarLevel(
                        usuario.getId(),
                        associacaoId,
                        request
                )
        );
    }

    @DeleteMapping("/{associacaoId}")
    public ResponseEntity<Void> excluir(
            Authentication authentication,
            @PathVariable Long associacaoId
    ) {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        usuarioSkillService.excluir(
                usuario.getId(),
                associacaoId
        );

        return ResponseEntity.noContent().build();
    }
}