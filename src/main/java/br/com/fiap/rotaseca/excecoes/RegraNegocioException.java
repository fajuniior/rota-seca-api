package br.com.fiap.rotaseca.excecoes;

/**
 * Dados enviados quebram uma regra de negócio (HTTP 400).
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
