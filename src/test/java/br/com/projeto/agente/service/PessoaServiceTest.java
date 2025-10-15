package br.com.projeto.agente.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.projeto.agente.domain.enums.Genero;
import br.com.projeto.agente.domain.model.Pessoa;
import br.com.projeto.agente.repository.PessoaRepository;
import br.com.projeto.agente.service.PessoaServiceImpl;
import br.com.projeto.agente.service.exception.PessoaNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    private Pessoa pessoaPadrao;

    @BeforeEach
    void setUp() {
        pessoaPadrao = Pessoa.builder()
                .id(1L)
                .nome("João Silva")
                .idade(30)
                .genero(Genero.MASCULINO)
                .endereco("Rua A, 123")
                .build();
    }

    @Test
    @DisplayName("Deve listar todas as pessoas")
    void deveListarTodasAsPessoas() {
        when(pessoaRepository.findAll()).thenReturn(List.of(pessoaPadrao));

        List<Pessoa> resultado = pessoaService.listarTodas();

        assertThat(resultado)
                .hasSize(1)
                .first()
                .extracting(Pessoa::getNome)
                .isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve buscar pessoa por id")
    void deveBuscarPessoaPorId() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoaPadrao));

        Pessoa resultado = pessoaService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pessoa não encontrada")
    void deveLancarExcecaoQuandoPessoaNaoEncontrada() {
        when(pessoaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pessoaService.buscarPorId(2L))
                .isInstanceOf(PessoaNotFoundException.class)
                .hasMessageContaining("2");
    }

    @Test
    @DisplayName("Deve criar uma nova pessoa")
    void deveCriarPessoa() {
        Pessoa paraSalvar = pessoaPadrao.toBuilder().id(null).build();
        when(pessoaRepository.save(paraSalvar)).thenReturn(pessoaPadrao);

        Pessoa resultado = pessoaService.criar(paraSalvar);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve atualizar uma pessoa existente")
    void deveAtualizarPessoa() {
        Pessoa dadosAtualizados = Pessoa.builder()
                .nome("Maria Souza")
                .idade(28)
                .genero(Genero.FEMININO)
                .endereco("Rua B, 456")
                .build();

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoaPadrao));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pessoa resultado = pessoaService.atualizar(1L, dadosAtualizados);

        assertThat(resultado.getNome()).isEqualTo("Maria Souza");
        assertThat(resultado.getGenero()).isEqualTo(Genero.FEMININO);
        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve remover pessoa existente")
    void deveRemoverPessoa() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoaPadrao));
        doNothing().when(pessoaRepository).delete(pessoaPadrao);

        pessoaService.remover(1L);

        verify(pessoaRepository).delete(eq(pessoaPadrao));
    }
}
