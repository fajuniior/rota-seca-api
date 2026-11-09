package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.LeituraChuva;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela LEITURA_CHUVA.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class LeituraChuvaDAO {

    private static final String COLUNAS = "ID_LEITURA, ID_BAIRRO, DT_LEITURA, PRECIPITACAO_MM, FONTE";

    // Insert
    public String inserir(LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO LEITURA_CHUVA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, leituraChuva.getIdLeitura());
            stmt.setInt(2, leituraChuva.getIdBairro());
            JdbcUtil.setDataHora(stmt, 3, leituraChuva.getDtLeitura());
            stmt.setDouble(4, leituraChuva.getPrecipitacaoMm());
            stmt.setString(5, leituraChuva.getFonte());
            stmt.execute();
        }
        return "LeituraChuva cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM LEITURA_CHUVA WHERE ID_LEITURA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE LEITURA_CHUVA SET ID_BAIRRO = ?, DT_LEITURA = ?, PRECIPITACAO_MM = ?, FONTE = ? WHERE ID_LEITURA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, leituraChuva.getIdBairro());
            JdbcUtil.setDataHora(stmt, 2, leituraChuva.getDtLeitura());
            stmt.setDouble(3, leituraChuva.getPrecipitacaoMm());
            stmt.setString(4, leituraChuva.getFonte());
            stmt.setInt(5, leituraChuva.getIdLeitura());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<LeituraChuva> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM LEITURA_CHUVA ORDER BY ID_LEITURA");
    }

    // Buscar por ID
    public LeituraChuva buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<LeituraChuva> lista = consultar("SELECT " + COLUNAS + " FROM LEITURA_CHUVA WHERE ID_LEITURA = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_LEITURA), 0) + 1 FROM LEITURA_CHUVA";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<LeituraChuva> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<LeituraChuva> lista = new ArrayList<LeituraChuva>();
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

    private LeituraChuva montar(ResultSet rs) throws SQLException {
        LeituraChuva leituraChuva = new LeituraChuva();
        leituraChuva.setIdLeitura(rs.getInt("ID_LEITURA"));
        leituraChuva.setIdBairro(rs.getInt("ID_BAIRRO"));
        leituraChuva.setDtLeitura(JdbcUtil.getDataHora(rs, "DT_LEITURA"));
        leituraChuva.setPrecipitacaoMm(rs.getDouble("PRECIPITACAO_MM"));
        leituraChuva.setFonte(rs.getString("FONTE"));
        return leituraChuva;
    }

    // Leituras de chuva de um bairro
    public List<LeituraChuva> selecionarPorBairro(int idBairro) throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM LEITURA_CHUVA WHERE ID_BAIRRO = ? ORDER BY DT_LEITURA", idBairro);
    }
}
