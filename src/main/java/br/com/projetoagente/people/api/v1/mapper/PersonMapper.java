package br.com.projetoagente.people.api.v1.mapper;

import br.com.projetoagente.people.api.v1.dto.PersonRequest;
import br.com.projetoagente.people.api.v1.dto.PersonResponse;
import br.com.projetoagente.people.domain.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public Person toEntity(PersonRequest request) {
        Person person = new Person();
        person.setName(request.name());
        person.setAge(request.age());
        person.setGender(request.gender());
        person.setAddress(request.address());
        return person;
    }

    public PersonResponse toResponse(Person person) {
        return new PersonResponse(person.getId(), person.getName(), person.getAge(), person.getGender(), person.getAddress());
    }
}
