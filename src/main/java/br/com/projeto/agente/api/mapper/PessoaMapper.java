package br.com.projeto.agente.api.mapper;

import br.com.projeto.agente.api.dto.PessoaRequest;
import br.com.projeto.agente.api.dto.PessoaResponse;
import br.com.projeto.agente.domain.model.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {

    public Pessoa toEntity(PessoaRequest request) {
        if (request == null) {
            return null;
        }
        return Pessoa.builder()
                .nome(request.getNome())
                .idade(request.getIdade())
                .genero(request.getGenero())
                .endereco(request.getEndereco())
                .build();
    }

    public PessoaResponse toResponse(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }
        return PessoaResponse.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .idade(pessoa.getIdade())
                .genero(pessoa.getGenero())
                .endereco(pessoa.getEndereco())
                .build();
    }
}
