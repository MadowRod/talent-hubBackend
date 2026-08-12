package br.com.talen_hub.categoria.controller;

import br.com.talen_hub.categoria.dto.request.CategoriaRequest;
import br.com.talen_hub.categoria.dto.response.CategoriaResponse;
import br.com.talen_hub.categoria.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(
            CategoriaService categoriaService
    ) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(
            @Valid @RequestBody CategoriaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {

        return ResponseEntity.ok(
                categoriaService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                categoriaService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request
    ) {

        return ResponseEntity.ok(
                categoriaService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {

        categoriaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}