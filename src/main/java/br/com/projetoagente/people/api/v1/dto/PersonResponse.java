package br.com.projetoagente.people.api.v1.dto;

import br.com.projetoagente.people.domain.model.Gender;

public record PersonResponse(Long id, String name, Integer age, Gender gender, String address) {
}
