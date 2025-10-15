package br.com.projetoagente.people.domain.service.impl;

import br.com.projetoagente.people.api.exception.ResourceNotFoundException;
import br.com.projetoagente.people.domain.model.Gender;
import br.com.projetoagente.people.domain.model.Person;
import br.com.projetoagente.people.domain.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setName("Ana");
        person.setAge(34);
        person.setGender(Gender.FEMALE);
        person.setAddress("Avenida Central, 123");
    }

    @Test
    @DisplayName("should create person when valid data provided")
    void createPerson() {
        given(personRepository.save(any(Person.class))).willReturn(person);

        Person saved = personService.create(person);

        assertThat(saved.getId()).isEqualTo(1L);
        verify(personRepository).save(any(Person.class));
    }

    @Test
    @DisplayName("should update existing person")
    void updatePerson() {
        Person updatePayload = new Person();
        updatePayload.setName("Ana Maria");
        updatePayload.setAge(35);
        updatePayload.setGender(Gender.FEMALE);
        updatePayload.setAddress("Rua Nova, 456");

        given(personRepository.findById(1L)).willReturn(Optional.of(person));
        given(personRepository.save(any(Person.class))).willAnswer(invocation -> invocation.getArgument(0));

        Person updated = personService.update(1L, updatePayload);

        assertThat(updated.getName()).isEqualTo("Ana Maria");
        assertThat(updated.getAge()).isEqualTo(35);
        verify(personRepository).save(any(Person.class));
    }

    @Test
    @DisplayName("should delete existing person")
    void deletePerson() {
        given(personRepository.findById(1L)).willReturn(Optional.of(person));

        personService.delete(1L);

        verify(personRepository).delete(eq(person));
    }

    @Test
    @DisplayName("should list all people")
    void listPeople() {
        given(personRepository.findAll()).willReturn(List.of(person));

        List<Person> people = personService.findAll();

        assertThat(people).hasSize(1);
    }

    @Test
    @DisplayName("should throw when person not found")
    void findByIdNotFound() {
        given(personRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> personService.findById(99L)).isInstanceOf(ResourceNotFoundException.class);
    }
}
