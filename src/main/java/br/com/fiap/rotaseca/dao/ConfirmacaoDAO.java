package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.Confirmacao;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela CONFIRMACAO.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class ConfirmacaoDAO {

    private static final String COLUNAS = "ID_CONFIRMACAO, ID_OCORRENCIA, ID_USUARIO, DT_CONFIRMACAO";

    // Insert
    public String inserir(Confirmacao confirmacao) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO CONFIRMACAO (" + COLUNAS + ") VALUES (?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, confirmacao.getIdConfirmacao());
            stmt.setInt(2, confirmacao.getIdOcorrencia());
            stmt.setInt(3, confirmacao.getIdUsuario());
            JdbcUtil.setDataHora(stmt, 4, confirmacao.getDtConfirmacao());
            stmt.execute();
        }
        return "Confirmacao cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM CONFIRMACAO WHERE ID_CONFIRMACAO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(Confirmacao confirmacao) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE CONFIRMACAO SET ID_OCORRENCIA = ?, ID_USUARIO = ?, DT_CONFIRMACAO = ? WHERE ID_CONFIRMACAO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, confirmacao.getIdOcorrencia());
            stmt.setInt(2, confirmacao.getIdUsuario());
            JdbcUtil.setDataHora(stmt, 3, confirmacao.getDtConfirmacao());
            stmt.setInt(4, confirmacao.getIdConfirmacao());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<Confirmacao> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM CONFIRMACAO ORDER BY ID_CONFIRMACAO");
    }

    // Buscar por ID
    public Confirmacao buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<Confirmacao> lista = consultar("SELECT " + COLUNAS + " FROM CONFIRMACAO WHERE ID_CONFIRMACAO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_CONFIRMACAO), 0) + 1 FROM CONFIRMACAO";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<Confirmacao> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<Confirmacao> lista = new ArrayList<Confirmacao>();
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            JdbcUtil.preencher(stmt, parametros);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    private Confirmacao montar(ResultSet rs) throws SQLException {
        Confirmacao confirmacao = new Confirmacao();
        confirmacao.setIdConfirmacao(rs.getInt("ID_CONFIRMACAO"));
        confirmacao.setIdOcorrencia(rs.getInt("ID_OCORRENCIA"));
        confirmacao.setIdUsuario(rs.getInt("ID_USUARIO"));
        confirmacao.setDtConfirmacao(JdbcUtil.getDataHora(rs, "DT_CONFIRMACAO"));
        return confirmacao;
    }

    // Confirmações de uma ocorrência
    public List<Confirmacao> selecionarPorOcorrencia(int idOcorrencia) throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM CONFIRMACAO WHERE ID_OCORRENCIA = ? ORDER BY DT_CONFIRMACAO",
                idOcorrencia);
    }
}
