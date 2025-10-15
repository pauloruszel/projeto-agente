package br.com.projetoagente.people.domain.service.impl;

import br.com.projetoagente.people.api.exception.ResourceNotFoundException;
import br.com.projetoagente.people.domain.model.Person;
import br.com.projetoagente.people.domain.repository.PersonRepository;
import br.com.projetoagente.people.domain.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Person create(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person update(Long id, Person person) {
        Person existing = findById(id);
        existing.setName(person.getName());
        existing.setAge(person.getAge());
        existing.setGender(person.getGender());
        existing.setAddress(person.getAddress());
        return personRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Person existing = findById(id);
        personRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Person findById(Long id) {
        return personRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return personRepository.findAll();
    }
}
