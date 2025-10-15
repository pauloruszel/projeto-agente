package br.com.projeto.agente.api.controller;

import br.com.projeto.agente.api.dto.PessoaRequest;
import br.com.projeto.agente.api.mapper.PessoaMapper;
import br.com.projeto.agente.domain.enums.Genero;
import br.com.projeto.agente.domain.model.Pessoa;
import br.com.projeto.agente.service.PessoaService;
import br.com.projeto.agente.service.exception.PessoaNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PessoaController.class)
@Import(PessoaMapper.class)
@ActiveProfiles("test")
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PessoaService pessoaService;

    @Test
    @DisplayName("Deve criar uma pessoa e retornar 201")
    void deveCriarPessoa() throws Exception {
        PessoaRequest request = PessoaRequest.builder()
                .nome("Alice")
                .idade(29)
                .genero(Genero.FEMININO)
                .endereco("Rua das Flores, 10")
                .build();

        Pessoa salvo = Pessoa.builder()
                .id(1L)
                .nome("Alice")
                .idade(29)
                .genero(Genero.FEMININO)
                .endereco("Rua das Flores, 10")
                .build();

        when(pessoaService.criar(any(Pessoa.class))).thenReturn(salvo);

        mockMvc.perform(post("/api/v1/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is("http://localhost/api/v1/pessoas/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Alice")));
    }

    @Test
    @DisplayName("Deve listar pessoas e retornar 200")
    void deveListarPessoas() throws Exception {
        Pessoa pessoa1 = Pessoa.builder()
                .id(1L)
                .nome("Alice")
                .idade(29)
                .genero(Genero.FEMININO)
                .endereco("Rua das Flores, 10")
                .build();
        Pessoa pessoa2 = Pessoa.builder()
                .id(2L)
                .nome("Bruno")
                .idade(35)
                .genero(Genero.MASCULINO)
                .endereco("Av. Central, 200")
                .build();

        when(pessoaService.listarTodas()).thenReturn(List.of(pessoa1, pessoa2));

        mockMvc.perform(get("/api/v1/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    @DisplayName("Deve buscar pessoa por id e retornar 200")
    void deveBuscarPessoaPorId() throws Exception {
        Pessoa pessoa = Pessoa.builder()
                .id(10L)
                .nome("Carlos")
                .idade(40)
                .genero(Genero.MASCULINO)
                .endereco("Rua A, 100")
                .build();

        when(pessoaService.buscarPorId(10L)).thenReturn(pessoa);

        mockMvc.perform(get("/api/v1/pessoas/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.nome", is("Carlos")));
    }

    @Test
    @DisplayName("Deve atualizar pessoa e retornar 200")
    void deveAtualizarPessoa() throws Exception {
        PessoaRequest request = PessoaRequest.builder()
                .nome("Daniela")
                .idade(33)
                .genero(Genero.FEMININO)
                .endereco("Rua Nova, 321")
                .build();

        Pessoa atualizado = Pessoa.builder()
                .id(5L)
                .nome("Daniela")
                .idade(33)
                .genero(Genero.FEMININO)
                .endereco("Rua Nova, 321")
                .build();

        when(pessoaService.atualizar(any(Long.class), any(Pessoa.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/pessoas/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.nome", is("Daniela")));
    }

    @Test
    @DisplayName("Deve remover pessoa e retornar 204")
    void deveRemoverPessoa() throws Exception {
        doNothing().when(pessoaService).remover(3L);

        mockMvc.perform(delete("/api/v1/pessoas/{id}", 3L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 quando pessoa não encontrada")
    void deveRetornarNotFoundQuandoPessoaNaoEncontrada() throws Exception {
        when(pessoaService.buscarPorId(99L)).thenThrow(new PessoaNotFoundException(99L));

        mockMvc.perform(get("/api/v1/pessoas/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Pessoa com id 99 não encontrada")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando payload inválido")
    void deveRetornarBadRequestQuandoPayloadInvalido() throws Exception {
        PessoaRequest request = PessoaRequest.builder()
                .nome("")
                .idade(-1)
                .genero(null)
                .endereco("")
                .build();

        mockMvc.perform(post("/api/v1/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors", hasSize(4)));
    }
}
