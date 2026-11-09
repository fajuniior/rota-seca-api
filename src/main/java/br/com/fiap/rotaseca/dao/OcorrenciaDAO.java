package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.Ocorrencia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela OCORRENCIA.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class OcorrenciaDAO {

    private static final String COLUNAS = "ID_OCORRENCIA, ID_TRECHO, ID_USUARIO, TIPO, DT_REGISTRO, URL_FOTO, ST_VALIDACAO, ID_AGENTE, DT_VALIDACAO, DT_EXPIRACAO";

    // Insert
    public String inserir(Ocorrencia ocorrencia) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO OCORRENCIA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, ocorrencia.getIdOcorrencia());
            stmt.setInt(2, ocorrencia.getIdTrecho());
            stmt.setInt(3, ocorrencia.getIdUsuario());
            stmt.setString(4, ocorrencia.getTipo());
            JdbcUtil.setDataHora(stmt, 5, ocorrencia.getDtRegistro());
            stmt.setString(6, ocorrencia.getUrlFoto());
            stmt.setString(7, ocorrencia.getStValidacao());
            JdbcUtil.setInteger(stmt, 8, ocorrencia.getIdAgente());
            JdbcUtil.setDataHora(stmt, 9, ocorrencia.getDtValidacao());
            JdbcUtil.setDataHora(stmt, 10, ocorrencia.getDtExpiracao());
            stmt.execute();
        }
        return "Ocorrencia cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM OCORRENCIA WHERE ID_OCORRENCIA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(Ocorrencia ocorrencia) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE OCORRENCIA SET ID_TRECHO = ?, ID_USUARIO = ?, TIPO = ?, DT_REGISTRO = ?, URL_FOTO = ?, ST_VALIDACAO = ?, ID_AGENTE = ?, DT_VALIDACAO = ?, DT_EXPIRACAO = ? WHERE ID_OCORRENCIA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, ocorrencia.getIdTrecho());
            stmt.setInt(2, ocorrencia.getIdUsuario());
            stmt.setString(3, ocorrencia.getTipo());
            JdbcUtil.setDataHora(stmt, 4, ocorrencia.getDtRegistro());
            stmt.setString(5, ocorrencia.getUrlFoto());
            stmt.setString(6, ocorrencia.getStValidacao());
            JdbcUtil.setInteger(stmt, 7, ocorrencia.getIdAgente());
            JdbcUtil.setDataHora(stmt, 8, ocorrencia.getDtValidacao());
            JdbcUtil.setDataHora(stmt, 9, ocorrencia.getDtExpiracao());
            stmt.setInt(10, ocorrencia.getIdOcorrencia());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<Ocorrencia> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM OCORRENCIA ORDER BY ID_OCORRENCIA");
    }

    // Buscar por ID
    public Ocorrencia buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<Ocorrencia> lista = consultar("SELECT " + COLUNAS + " FROM OCORRENCIA WHERE ID_OCORRENCIA = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_OCORRENCIA), 0) + 1 FROM OCORRENCIA";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<Ocorrencia> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<Ocorrencia> lista = new ArrayList<Ocorrencia>();
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

    private Ocorrencia montar(ResultSet rs) throws SQLException {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setIdOcorrencia(rs.getInt("ID_OCORRENCIA"));
        ocorrencia.setIdTrecho(rs.getInt("ID_TRECHO"));
        ocorrencia.setIdUsuario(rs.getInt("ID_USUARIO"));
        ocorrencia.setTipo(rs.getString("TIPO"));
        ocorrencia.setDtRegistro(JdbcUtil.getDataHora(rs, "DT_REGISTRO"));
        ocorrencia.setUrlFoto(rs.getString("URL_FOTO"));
        ocorrencia.setStValidacao(rs.getString("ST_VALIDACAO"));
        ocorrencia.setIdAgente(JdbcUtil.getInteger(rs, "ID_AGENTE"));
        ocorrencia.setDtValidacao(JdbcUtil.getDataHora(rs, "DT_VALIDACAO"));
        ocorrencia.setDtExpiracao(JdbcUtil.getDataHora(rs, "DT_EXPIRACAO"));
        return ocorrencia;
    }

    // Ocorrências ativas: não descartadas e ainda não expiradas
    public List<Ocorrencia> selecionarAtivas(java.time.LocalDateTime agora) throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM OCORRENCIA WHERE ST_VALIDACAO <> 'DESCARTADA' "
                + "AND DT_EXPIRACAO > ? ORDER BY DT_REGISTRO DESC", java.sql.Timestamp.valueOf(agora));
    }

    // Grava o resultado da validação feita por um agente da Defesa Civil
    public boolean validar(int idOcorrencia, String status, int idAgente, java.time.LocalDateTime data)
            throws SQLException, ExcecoesConexao {
        String sql = "UPDATE OCORRENCIA SET ST_VALIDACAO = ?, ID_AGENTE = ?, DT_VALIDACAO = ? WHERE ID_OCORRENCIA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idAgente);
            JdbcUtil.setDataHora(stmt, 3, data);
            stmt.setInt(4, idOcorrencia);
            return stmt.executeUpdate() > 0;
        }
    }
}
