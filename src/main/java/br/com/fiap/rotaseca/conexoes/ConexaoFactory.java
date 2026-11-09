package br.com.fiap.rotaseca.conexoes;

import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões com o Oracle da FIAP.
 *
 * Usuário e senha NÃO ficam no código (o repositório do GitHub é público):
 * são lidos das variáveis de ambiente DB_USER e DB_PASSWORD.
 * - No IntelliJ: Run > Edit Configurations > Environment variables
 * - No Render: Environment > Environment Variables
 */
public class ConexaoFactory {

    private static final String URL_PADRAO = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    private static String variavel(String nome, String padrao) {
        String valor = System.getenv(nome);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }

    // metodo de conexão com o banco de dados
    public Connection conexao() throws ExcecoesConexao {
        String url = variavel("DB_URL", URL_PADRAO);
        String usuario = variavel("DB_USER", null);
        String senha = variavel("DB_PASSWORD", null);
        if (usuario == null || senha == null) {
            throw new ExcecoesConexao("Variáveis DB_USER e DB_PASSWORD não configuradas");
        }
        try {
            // Driver oracle (no teste local com H2 a URL começa com jdbc:h2)
            if (url.startsWith("jdbc:oracle")) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }
            // Retornar conexão
            return DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ExcecoesConexao(e);
        }
    }
}
