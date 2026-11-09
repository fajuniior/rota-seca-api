package br.com.fiap.rotaseca.excecoes;

/**
 * A operação é exclusiva de agentes da Defesa Civil e a credencial não foi informada ou está errada (HTTP 403).
 */
public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
