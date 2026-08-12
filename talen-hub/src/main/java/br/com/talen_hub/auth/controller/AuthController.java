package br.com.talen_hub.auth.controller;

import br.com.talen_hub.auth.dto.request.CadastroRequest;
import br.com.talen_hub.auth.dto.request.LoginRequest;
import br.com.talen_hub.auth.dto.response.LoginResponse;
import br.com.talen_hub.auth.dto.response.UsuarioResponse;
import br.com.talen_hub.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody CadastroRequest request
    ) {

        UsuarioResponse response = authService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}