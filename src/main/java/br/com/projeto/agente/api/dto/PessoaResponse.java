package br.com.projeto.agente.api.dto;

import br.com.projeto.agente.domain.enums.Genero;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PessoaResponse {

    private final Long id;
    private final String nome;
    private final Integer idade;
    private final Genero genero;
    private final String endereco;
}
