package br.com.projeto.agente.service;

import br.com.projeto.agente.domain.model.Pessoa;
import br.com.projeto.agente.repository.PessoaRepository;
import br.com.projeto.agente.service.exception.PessoaNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;

    @Override
    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    @Override
    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException(id));
    }

    @Override
    @Transactional
    public Pessoa criar(Pessoa novaPessoa) {
        return pessoaRepository.save(novaPessoa);
    }

    @Override
    @Transactional
    public Pessoa atualizar(Long id, Pessoa dadosAtualizados) {
        Pessoa existente = buscarPorId(id);
        aplicarEdicoes(existente, dadosAtualizados);
        return pessoaRepository.save(existente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Pessoa existente = buscarPorId(id);
        pessoaRepository.delete(existente);
    }

    private void aplicarEdicoes(Pessoa destino, Pessoa origem) {
        destino.setNome(origem.getNome());
        destino.setIdade(origem.getIdade());
        destino.setGenero(origem.getGenero());
        destino.setEndereco(origem.getEndereco());
    }
}
