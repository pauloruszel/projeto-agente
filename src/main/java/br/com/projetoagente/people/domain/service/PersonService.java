package br.com.projetoagente.people.domain.service;

import br.com.projetoagente.people.domain.model.Person;

import java.util.List;

public interface PersonService {

    Person create(Person person);

    Person update(Long id, Person person);

    void delete(Long id);

    Person findById(Long id);

    List<Person> findAll();
}
