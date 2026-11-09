package br.com.fiap.rotaseca.excecoes;

/**
 * Falha ao conectar no banco de dados (baseada na classe vista em aula).
 * O tratador global devolve HTTP 503 com a mensagem amigável.
 */
public class ExcecoesConexao extends Exception {

    public ExcecoesConexao(String mensagem) {
        super(mensagem);
    }

    public ExcecoesConexao(Exception e) {
        super(mensagemPara(e), e);
    }

    private static String mensagemPara(Exception e) {
        if (e instanceof ClassNotFoundException) {
            return "Sem comunicação com o Banco de Dados (driver Oracle não encontrado)";
        } else if (e instanceof java.sql.SQLException sql && sql.getErrorCode() == 1017) {
            return "Informações de usuário incorretas, acesso negado";
        } else {
            return "Falha ao conectar no Banco de Dados";
        }
    }
}
