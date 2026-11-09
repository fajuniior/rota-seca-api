package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.Bairro;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela BAIRRO.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class BairroDAO {

    private static final String COLUNAS = "ID_BAIRRO, NOME, CIDADE";

    // Insert
    public String inserir(Bairro bairro) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO BAIRRO (" + COLUNAS + ") VALUES (?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, bairro.getIdBairro());
            stmt.setString(2, bairro.getNome());
            stmt.setString(3, bairro.getCidade());
            stmt.execute();
        }
        return "Bairro cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM BAIRRO WHERE ID_BAIRRO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(Bairro bairro) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE BAIRRO SET NOME = ?, CIDADE = ? WHERE ID_BAIRRO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, bairro.getNome());
            stmt.setString(2, bairro.getCidade());
            stmt.setInt(3, bairro.getIdBairro());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<Bairro> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM BAIRRO ORDER BY ID_BAIRRO");
    }

    // Buscar por ID
    public Bairro buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<Bairro> lista = consultar("SELECT " + COLUNAS + " FROM BAIRRO WHERE ID_BAIRRO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_BAIRRO), 0) + 1 FROM BAIRRO";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<Bairro> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<Bairro> lista = new ArrayList<Bairro>();
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

    private Bairro montar(ResultSet rs) throws SQLException {
        Bairro bairro = new Bairro();
        bairro.setIdBairro(rs.getInt("ID_BAIRRO"));
        bairro.setNome(rs.getString("NOME"));
        bairro.setCidade(rs.getString("CIDADE"));
        return bairro;
    }
}
