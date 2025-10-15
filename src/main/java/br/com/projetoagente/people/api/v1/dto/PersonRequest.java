package br.com.projetoagente.people.api.v1.dto;

import br.com.projetoagente.people.domain.model.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PersonRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull @Min(0) @Max(150) Integer age,
        @NotNull Gender gender,
        @NotBlank @Size(max = 255) String address) {
}
