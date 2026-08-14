package br.com.talen_hub.admin.controller;

import br.com.talen_hub.admin.dto.UsuarioAdminResponse;
import br.com.talen_hub.admin.service.UsuarioAdminService;
import br.com.talen_hub.usuarioSkill.dto.response.UsuarioSkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
public class UsuarioAdminController {

    private final UsuarioAdminService usuarioAdminService;

    public UsuarioAdminController(
            UsuarioAdminService usuarioAdminService
    ) {
        this.usuarioAdminService = usuarioAdminService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioAdminResponse>> listarUsuarios(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String skill
    ) {

        return ResponseEntity.ok(
                usuarioAdminService.listarUsuarios(
                        categoria,
                        skill
                )
        );
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioAdminResponse> buscarUsuarioPorId(
            @PathVariable Long usuarioId
    ) {

        return ResponseEntity.ok(
                usuarioAdminService.buscarUsuarioPorId(
                        usuarioId
                )
        );
    }

    @GetMapping("/{usuarioId}/skills")
    public ResponseEntity<List<UsuarioSkillResponse>> listarSkillsUsuario(
            @PathVariable Long usuarioId
    ) {

        return ResponseEntity.ok(
                usuarioAdminService.listarSkillsUsuario(
                        usuarioId
                )
        );
    }
}