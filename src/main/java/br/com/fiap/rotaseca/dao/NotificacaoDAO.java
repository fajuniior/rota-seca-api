package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.Notificacao;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela NOTIFICACAO.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class NotificacaoDAO {

    private static final String COLUNAS = "ID_NOTIFICACAO, ID_ALERTA, ID_OCORRENCIA, ID_USUARIO, ID_CONTATO, CANAL, DT_ENVIO, ST_ENTREGA";

    // Insert
    public String inserir(Notificacao notificacao) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO NOTIFICACAO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, notificacao.getIdNotificacao());
            JdbcUtil.setInteger(stmt, 2, notificacao.getIdAlerta());
            JdbcUtil.setInteger(stmt, 3, notificacao.getIdOcorrencia());
            JdbcUtil.setInteger(stmt, 4, notificacao.getIdUsuario());
            JdbcUtil.setInteger(stmt, 5, notificacao.getIdContato());
            stmt.setString(6, notificacao.getCanal());
            JdbcUtil.setDataHora(stmt, 7, notificacao.getDtEnvio());
            stmt.setString(8, notificacao.getStEntrega());
            stmt.execute();
        }
        return "Notificacao cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(long id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM NOTIFICACAO WHERE ID_NOTIFICACAO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(Notificacao notificacao) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE NOTIFICACAO SET ID_ALERTA = ?, ID_OCORRENCIA = ?, ID_USUARIO = ?, ID_CONTATO = ?, CANAL = ?, DT_ENVIO = ?, ST_ENTREGA = ? WHERE ID_NOTIFICACAO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            JdbcUtil.setInteger(stmt, 1, notificacao.getIdAlerta());
            JdbcUtil.setInteger(stmt, 2, notificacao.getIdOcorrencia());
            JdbcUtil.setInteger(stmt, 3, notificacao.getIdUsuario());
            JdbcUtil.setInteger(stmt, 4, notificacao.getIdContato());
            stmt.setString(5, notificacao.getCanal());
            JdbcUtil.setDataHora(stmt, 6, notificacao.getDtEnvio());
            stmt.setString(7, notificacao.getStEntrega());
            stmt.setLong(8, notificacao.getIdNotificacao());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<Notificacao> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM NOTIFICACAO ORDER BY ID_NOTIFICACAO");
    }

    // Buscar por ID
    public Notificacao buscarPorId(long id) throws SQLException, ExcecoesConexao {
        List<Notificacao> lista = consultar("SELECT " + COLUNAS + " FROM NOTIFICACAO WHERE ID_NOTIFICACAO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public long proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_NOTIFICACAO), 0) + 1 FROM NOTIFICACAO";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<Notificacao> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<Notificacao> lista = new ArrayList<Notificacao>();
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

    private Notificacao montar(ResultSet rs) throws SQLException {
        Notificacao notificacao = new Notificacao();
        notificacao.setIdNotificacao(rs.getLong("ID_NOTIFICACAO"));
        notificacao.setIdAlerta(JdbcUtil.getInteger(rs, "ID_ALERTA"));
        notificacao.setIdOcorrencia(JdbcUtil.getInteger(rs, "ID_OCORRENCIA"));
        notificacao.setIdUsuario(JdbcUtil.getInteger(rs, "ID_USUARIO"));
        notificacao.setIdContato(JdbcUtil.getInteger(rs, "ID_CONTATO"));
        notificacao.setCanal(rs.getString("CANAL"));
        notificacao.setDtEnvio(JdbcUtil.getDataHora(rs, "DT_ENVIO"));
        notificacao.setStEntrega(rs.getString("ST_ENTREGA"));
        return notificacao;
    }
}
