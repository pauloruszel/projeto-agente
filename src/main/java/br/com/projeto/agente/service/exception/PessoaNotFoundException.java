package br.com.projeto.agente.service.exception;

public class PessoaNotFoundException extends RuntimeException {

    public PessoaNotFoundException(Long id) {
        super("Pessoa com id %d não encontrada".formatted(id));
    }
}
