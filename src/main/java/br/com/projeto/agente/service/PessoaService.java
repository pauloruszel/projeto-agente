package br.com.projeto.agente.service;

import br.com.projeto.agente.domain.model.Pessoa;
import java.util.List;

public interface PessoaService {

    List<Pessoa> listarTodas();

    Pessoa buscarPorId(Long id);

    Pessoa criar(Pessoa novaPessoa);

    Pessoa atualizar(Long id, Pessoa dadosAtualizados);

    void remover(Long id);
}
