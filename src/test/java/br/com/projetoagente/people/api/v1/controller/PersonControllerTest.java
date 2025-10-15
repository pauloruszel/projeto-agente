package br.com.projetoagente.people.api.v1.controller;

import br.com.projetoagente.people.api.exception.ResourceNotFoundException;
import br.com.projetoagente.people.api.v1.dto.PersonRequest;
import br.com.projetoagente.people.api.v1.mapper.PersonMapper;
import br.com.projetoagente.people.domain.model.Gender;
import br.com.projetoagente.people.domain.model.Person;
import br.com.projetoagente.people.domain.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@Import(PersonMapper.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    @DisplayName("should create person when payload is valid")
    void createPerson() throws Exception {
        PersonRequest request = new PersonRequest("Alice", 30, Gender.FEMALE, "Rua A, 100");

        Person saved = new Person();
        saved.setId(1L);
        saved.setName(request.name());
        saved.setAge(request.age());
        saved.setGender(request.gender());
        saved.setAddress(request.address());

        given(personService.create(any(Person.class))).willReturn(saved);

        mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.gender", is("FEMALE")));

        verify(personService).create(any(Person.class));
    }

    @Test
    @DisplayName("should return validation error when payload is invalid")
    void createPersonValidationError() throws Exception {
        PersonRequest request = new PersonRequest("", -1, null, "A");

        mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    @DisplayName("should return person when found")
    void getPerson() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setName("Bob");
        person.setAge(25);
        person.setGender(Gender.MALE);
        person.setAddress("Rua B, 200");

        given(personService.findById(1L)).willReturn(person);

        mockMvc.perform(get("/v1/persons/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bob")))
                .andExpect(jsonPath("$.gender", is("MALE")));
    }

    @Test
    @DisplayName("should return 404 when person is missing")
    void getPersonNotFound() throws Exception {
        given(personService.findById(99L)).willThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(get("/v1/persons/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("should update person when payload is valid")
    void updatePerson() throws Exception {
        PersonRequest request = new PersonRequest("Carol", 28, Gender.FEMALE, "Rua C, 300");

        Person updated = new Person();
        updated.setId(2L);
        updated.setName(request.name());
        updated.setAge(request.age());
        updated.setGender(request.gender());
        updated.setAddress(request.address());

        given(personService.update(eq(2L), any(Person.class))).willReturn(updated);

        mockMvc.perform(put("/v1/persons/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Carol")));
    }

    @Test
    @DisplayName("should delete person and return 204")
    void deletePerson() throws Exception {
        mockMvc.perform(delete("/v1/persons/{id}", 1)).andExpect(status().isNoContent());

        verify(personService).delete(1L);
    }
}
