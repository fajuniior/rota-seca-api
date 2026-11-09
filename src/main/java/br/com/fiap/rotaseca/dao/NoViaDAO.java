package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.NoVia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela NO_VIA.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class NoViaDAO {

    private static final String COLUNAS = "ID_NO, ID_BAIRRO, REFERENCIA, LATITUDE, LONGITUDE";

    // Insert
    public String inserir(NoVia noVia) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO NO_VIA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, noVia.getIdNo());
            stmt.setInt(2, noVia.getIdBairro());
            stmt.setString(3, noVia.getReferencia());
            stmt.setDouble(4, noVia.getLatitude());
            stmt.setDouble(5, noVia.getLongitude());
            stmt.execute();
        }
        return "NoVia cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM NO_VIA WHERE ID_NO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(NoVia noVia) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE NO_VIA SET ID_BAIRRO = ?, REFERENCIA = ?, LATITUDE = ?, LONGITUDE = ? WHERE ID_NO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, noVia.getIdBairro());
            stmt.setString(2, noVia.getReferencia());
            stmt.setDouble(3, noVia.getLatitude());
            stmt.setDouble(4, noVia.getLongitude());
            stmt.setInt(5, noVia.getIdNo());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<NoVia> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM NO_VIA ORDER BY ID_NO");
    }

    // Buscar por ID
    public NoVia buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<NoVia> lista = consultar("SELECT " + COLUNAS + " FROM NO_VIA WHERE ID_NO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_NO), 0) + 1 FROM NO_VIA";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<NoVia> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<NoVia> lista = new ArrayList<NoVia>();
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

    private NoVia montar(ResultSet rs) throws SQLException {
        NoVia noVia = new NoVia();
        noVia.setIdNo(rs.getInt("ID_NO"));
        noVia.setIdBairro(rs.getInt("ID_BAIRRO"));
        noVia.setReferencia(rs.getString("REFERENCIA"));
        noVia.setLatitude(rs.getDouble("LATITUDE"));
        noVia.setLongitude(rs.getDouble("LONGITUDE"));
        return noVia;
    }
}
