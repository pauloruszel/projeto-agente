package br.com.projeto.agente.repository;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.projeto.agente.domain.enums.Genero;
import br.com.projeto.agente.domain.model.Pessoa;
import br.com.projeto.agente.repository.PessoaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve salvar pessoa e gerar ID")
    void deveSalvarPessoa() {
        Pessoa pessoa = novaPessoa("Ana", Genero.FEMININO);

        Pessoa salva = pessoaRepository.save(pessoa);

        assertThat(salva.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve encontrar pessoa por ID")
    void deveEncontrarPessoaPorId() {
        Pessoa persistida = entityManager.persistAndFlush(novaPessoa("Carlos", Genero.MASCULINO));

        assertThat(pessoaRepository.findById(persistida.getId()))
                .isPresent()
                .get()
                .extracting(Pessoa::getNome)
                .isEqualTo("Carlos");
    }

    @Test
    @DisplayName("Deve listar pessoas")
    void deveListarPessoas() {
        entityManager.persistAndFlush(novaPessoa("Joana", Genero.FEMININO));
        entityManager.persistAndFlush(novaPessoa("Pedro", Genero.MASCULINO));

        assertThat(pessoaRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("Deve remover pessoa")
    void deveRemoverPessoa() {
        Pessoa persistida = entityManager.persistAndFlush(novaPessoa("Samuel", Genero.OUTRO));

        pessoaRepository.delete(persistida);
        entityManager.flush();

        assertThat(pessoaRepository.findById(persistida.getId())).isEmpty();
    }

    private Pessoa novaPessoa(String nome, Genero genero) {
        return Pessoa.builder()
                .nome(nome)
                .idade(25)
                .genero(genero)
                .endereco("Rua de Teste, 100")
                .build();
    }
}
