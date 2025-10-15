package br.com.projeto.agente.api.controller;

import br.com.projeto.agente.api.dto.PessoaRequest;
import br.com.projeto.agente.api.dto.PessoaResponse;
import br.com.projeto.agente.api.mapper.PessoaMapper;
import br.com.projeto.agente.domain.model.Pessoa;
import br.com.projeto.agente.service.PessoaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;
    private final PessoaMapper pessoaMapper;

    @PostMapping
    public ResponseEntity<PessoaResponse> criar(@Valid @RequestBody PessoaRequest request) {
        Pessoa novaPessoa = pessoaMapper.toEntity(request);
        Pessoa pessoaSalva = pessoaService.criar(novaPessoa);
        PessoaResponse response = pessoaMapper.toResponse(pessoaSalva);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponse>> listarTodas() {
        List<PessoaResponse> respostas = pessoaService.listarTodas().stream()
                .map(pessoaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respostas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        return ResponseEntity.ok(pessoaMapper.toResponse(pessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaRequest request) {
        Pessoa dadosAtualizados = pessoaMapper.toEntity(request);
        Pessoa pessoaAtualizada = pessoaService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(pessoaMapper.toResponse(pessoaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        pessoaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
