package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.PontoApoio;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela PONTO_APOIO.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class PontoApoioDAO {

    private static final String COLUNAS = "ID_PONTO, ID_BAIRRO, ID_NO, NOME, TIPO, ENDERECO, CAPACIDADE, HORARIO, ST_ABERTO, TEM_BANHEIRO, DT_ATUALIZACAO";

    // Insert
    public String inserir(PontoApoio pontoApoio) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO PONTO_APOIO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pontoApoio.getIdPonto());
            stmt.setInt(2, pontoApoio.getIdBairro());
            JdbcUtil.setInteger(stmt, 3, pontoApoio.getIdNo());
            stmt.setString(4, pontoApoio.getNome());
            stmt.setString(5, pontoApoio.getTipo());
            stmt.setString(6, pontoApoio.getEndereco());
            JdbcUtil.setInteger(stmt, 7, pontoApoio.getCapacidade());
            stmt.setString(8, pontoApoio.getHorario());
            stmt.setString(9, pontoApoio.getStAberto());
            stmt.setString(10, pontoApoio.getTemBanheiro());
            JdbcUtil.setDataHora(stmt, 11, pontoApoio.getDtAtualizacao());
            stmt.execute();
        }
        return "PontoApoio cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM PONTO_APOIO WHERE ID_PONTO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(PontoApoio pontoApoio) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE PONTO_APOIO SET ID_BAIRRO = ?, ID_NO = ?, NOME = ?, TIPO = ?, ENDERECO = ?, CAPACIDADE = ?, HORARIO = ?, ST_ABERTO = ?, TEM_BANHEIRO = ?, DT_ATUALIZACAO = ? WHERE ID_PONTO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, pontoApoio.getIdBairro());
            JdbcUtil.setInteger(stmt, 2, pontoApoio.getIdNo());
            stmt.setString(3, pontoApoio.getNome());
            stmt.setString(4, pontoApoio.getTipo());
            stmt.setString(5, pontoApoio.getEndereco());
            JdbcUtil.setInteger(stmt, 6, pontoApoio.getCapacidade());
            stmt.setString(7, pontoApoio.getHorario());
            stmt.setString(8, pontoApoio.getStAberto());
            stmt.setString(9, pontoApoio.getTemBanheiro());
            JdbcUtil.setDataHora(stmt, 10, pontoApoio.getDtAtualizacao());
            stmt.setInt(11, pontoApoio.getIdPonto());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<PontoApoio> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM PONTO_APOIO ORDER BY ID_PONTO");
    }

    // Buscar por ID
    public PontoApoio buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<PontoApoio> lista = consultar("SELECT " + COLUNAS + " FROM PONTO_APOIO WHERE ID_PONTO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_PONTO), 0) + 1 FROM PONTO_APOIO";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<PontoApoio> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<PontoApoio> lista = new ArrayList<PontoApoio>();
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

    private PontoApoio montar(ResultSet rs) throws SQLException {
        PontoApoio pontoApoio = new PontoApoio();
        pontoApoio.setIdPonto(rs.getInt("ID_PONTO"));
        pontoApoio.setIdBairro(rs.getInt("ID_BAIRRO"));
        pontoApoio.setIdNo(JdbcUtil.getInteger(rs, "ID_NO"));
        pontoApoio.setNome(rs.getString("NOME"));
        pontoApoio.setTipo(rs.getString("TIPO"));
        pontoApoio.setEndereco(rs.getString("ENDERECO"));
        pontoApoio.setCapacidade(JdbcUtil.getInteger(rs, "CAPACIDADE"));
        pontoApoio.setHorario(rs.getString("HORARIO"));
        pontoApoio.setStAberto(rs.getString("ST_ABERTO"));
        pontoApoio.setTemBanheiro(rs.getString("TEM_BANHEIRO"));
        pontoApoio.setDtAtualizacao(JdbcUtil.getDataHora(rs, "DT_ATUALIZACAO"));
        return pontoApoio;
    }

    // Pontos de apoio com filtros opcionais de bairro e de status aberto
    public List<PontoApoio> selecionarFiltrado(Integer idBairro, String aberto) throws SQLException, ExcecoesConexao {
        String sql = "SELECT " + COLUNAS + " FROM PONTO_APOIO WHERE (? IS NULL OR ID_BAIRRO = ?) "
                + "AND (? IS NULL OR ST_ABERTO = ?) ORDER BY ID_BAIRRO, NOME";
        return consultar(sql, idBairro, idBairro, aberto, aberto);
    }

    // Abre ou fecha um ponto de apoio
    public boolean atualizarStatus(int idPonto, String aberto, java.time.LocalDateTime data)
            throws SQLException, ExcecoesConexao {
        String sql = "UPDATE PONTO_APOIO SET ST_ABERTO = ?, DT_ATUALIZACAO = ? WHERE ID_PONTO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, aberto);
            JdbcUtil.setDataHora(stmt, 2, data);
            stmt.setInt(3, idPonto);
            return stmt.executeUpdate() > 0;
        }
    }
}
