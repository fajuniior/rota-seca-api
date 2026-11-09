package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.TrechoVia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela TRECHO_VIA.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class TrechoViaDAO {

    private static final String COLUNAS = "ID_TRECHO, ID_NO_ORIGEM, ID_NO_DESTINO, NOME_VIA, DISTANCIA_M, NIVEL_RISCO, ST_TRECHO";

    // Insert
    public String inserir(TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO TRECHO_VIA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, trechoVia.getIdTrecho());
            stmt.setInt(2, trechoVia.getIdNoOrigem());
            stmt.setInt(3, trechoVia.getIdNoDestino());
            stmt.setString(4, trechoVia.getNomeVia());
            stmt.setInt(5, trechoVia.getDistanciaM());
            stmt.setInt(6, trechoVia.getNivelRisco());
            stmt.setString(7, trechoVia.getStTrecho());
            stmt.execute();
        }
        return "TrechoVia cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM TRECHO_VIA WHERE ID_TRECHO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE TRECHO_VIA SET ID_NO_ORIGEM = ?, ID_NO_DESTINO = ?, NOME_VIA = ?, DISTANCIA_M = ?, NIVEL_RISCO = ?, ST_TRECHO = ? WHERE ID_TRECHO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, trechoVia.getIdNoOrigem());
            stmt.setInt(2, trechoVia.getIdNoDestino());
            stmt.setString(3, trechoVia.getNomeVia());
            stmt.setInt(4, trechoVia.getDistanciaM());
            stmt.setInt(5, trechoVia.getNivelRisco());
            stmt.setString(6, trechoVia.getStTrecho());
            stmt.setInt(7, trechoVia.getIdTrecho());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<TrechoVia> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM TRECHO_VIA ORDER BY ID_TRECHO");
    }

    // Buscar por ID
    public TrechoVia buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<TrechoVia> lista = consultar("SELECT " + COLUNAS + " FROM TRECHO_VIA WHERE ID_TRECHO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_TRECHO), 0) + 1 FROM TRECHO_VIA";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<TrechoVia> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<TrechoVia> lista = new ArrayList<TrechoVia>();
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

    private TrechoVia montar(ResultSet rs) throws SQLException {
        TrechoVia trechoVia = new TrechoVia();
        trechoVia.setIdTrecho(rs.getInt("ID_TRECHO"));
        trechoVia.setIdNoOrigem(rs.getInt("ID_NO_ORIGEM"));
        trechoVia.setIdNoDestino(rs.getInt("ID_NO_DESTINO"));
        trechoVia.setNomeVia(rs.getString("NOME_VIA"));
        trechoVia.setDistanciaM(rs.getInt("DISTANCIA_M"));
        trechoVia.setNivelRisco(rs.getInt("NIVEL_RISCO"));
        trechoVia.setStTrecho(rs.getString("ST_TRECHO"));
        return trechoVia;
    }
}
