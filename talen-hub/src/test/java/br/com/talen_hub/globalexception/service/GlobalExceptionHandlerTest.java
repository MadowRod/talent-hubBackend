package br.com.talen_hub.globalexception.service;

import br.com.talen_hub.exception.BusinessException;
import br.com.talen_hub.exception.GlobalExceptionHandler;
import br.com.talen_hub.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new TestExceptionController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void deveRetornar404QuandoRecursoNaoForEncontrado() throws Exception {

        mockMvc.perform(get("/teste/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Recurso não encontrado"));
    }

    @Test
    void deveRetornar409QuandoOcorrerErroDeRegraDeNegocio() throws Exception {

        mockMvc.perform(get("/teste/business"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Regra de negócio violada"));
    }

    @Test
    void deveRetornar400QuandoValidacaoFalhar() throws Exception {

        mockMvc.perform(
                        post("/teste/validation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "nome": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errors[0]")
                        .value(containsString("nome")));
    }

    @Test
    void deveRetornar500QuandoOcorrerErroInterno() throws Exception {

        mockMvc.perform(get("/teste/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error")
                        .value("Internal Server Error"))
                .andExpect(jsonPath("$.message")
                        .value("Ocorreu um erro interno no servidor."));
    }

    @RestController
    @RequestMapping("/teste")
    static class TestExceptionController {

        @GetMapping("/not-found")
        public void notFound() {
            throw new ResourceNotFoundException(
                    "Recurso não encontrado"
            );
        }

        @GetMapping("/business")
        public void business() {
            throw new BusinessException(
                    "Regra de negócio violada"
            );
        }

        @PostMapping("/validation")
        public void validation(
                @Valid @RequestBody TestRequest request
        ) {
        }

        @GetMapping("/generic")
        public void generic() {
            throw new RuntimeException("Erro inesperado");
        }
    }

    record TestRequest(
            @NotBlank(message = "Nome é obrigatório")
            String nome
    ) {
    }
}