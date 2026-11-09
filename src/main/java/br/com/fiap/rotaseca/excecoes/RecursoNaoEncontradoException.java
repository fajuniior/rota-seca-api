package br.com.fiap.rotaseca.excecoes;

/**
 * O registro pedido não existe (HTTP 404).
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
