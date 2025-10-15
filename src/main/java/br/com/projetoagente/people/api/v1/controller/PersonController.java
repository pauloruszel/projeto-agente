package br.com.projetoagente.people.api.v1.controller;

import br.com.projetoagente.people.api.v1.dto.PersonRequest;
import br.com.projetoagente.people.api.v1.dto.PersonResponse;
import br.com.projetoagente.people.api.v1.mapper.PersonMapper;
import br.com.projetoagente.people.domain.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/persons")
@Validated
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @GetMapping
    public List<PersonResponse> listAll() {
        return personService.findAll().stream().map(personMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PersonResponse getById(@PathVariable Long id) {
        return personMapper.toResponse(personService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse create(@Valid @RequestBody PersonRequest request) {
        return personMapper.toResponse(personService.create(personMapper.toEntity(request)));
    }

    @PutMapping("/{id}")
    public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest request) {
        return personMapper.toResponse(personService.update(id, personMapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personService.delete(id);
    }
}
