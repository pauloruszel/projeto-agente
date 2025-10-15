package br.com.projeto.agente.api.dto;

import br.com.projeto.agente.domain.enums.Genero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequest {

    @NotBlank
    @Size(max = 120)
    private String nome;

    @NotNull
    @Min(0)
    private Integer idade;

    @NotNull
    private Genero genero;

    @NotBlank
    @Size(max = 180)
    private String endereco;
}
