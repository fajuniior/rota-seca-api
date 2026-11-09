package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.CaminhoSalvo;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela CAMINHO_SALVO.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class CaminhoSalvoDAO {

    private static final String COLUNAS = "ID_CAMINHO, ID_USUARIO, NOME, ID_NO_ORIGEM, ID_NO_DESTINO, DT_CRIACAO";

    // Insert
    public String inserir(CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO CAMINHO_SALVO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, caminhoSalvo.getIdCaminho());
            stmt.setInt(2, caminhoSalvo.getIdUsuario());
            stmt.setString(3, caminhoSalvo.getNome());
            stmt.setInt(4, caminhoSalvo.getIdNoOrigem());
            stmt.setInt(5, caminhoSalvo.getIdNoDestino());
            JdbcUtil.setDataHora(stmt, 6, caminhoSalvo.getDtCriacao());
            stmt.execute();
        }
        return "CaminhoSalvo cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM CAMINHO_SALVO WHERE ID_CAMINHO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE CAMINHO_SALVO SET ID_USUARIO = ?, NOME = ?, ID_NO_ORIGEM = ?, ID_NO_DESTINO = ?, DT_CRIACAO = ? WHERE ID_CAMINHO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, caminhoSalvo.getIdUsuario());
            stmt.setString(2, caminhoSalvo.getNome());
            stmt.setInt(3, caminhoSalvo.getIdNoOrigem());
            stmt.setInt(4, caminhoSalvo.getIdNoDestino());
            JdbcUtil.setDataHora(stmt, 5, caminhoSalvo.getDtCriacao());
            stmt.setInt(6, caminhoSalvo.getIdCaminho());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<CaminhoSalvo> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM CAMINHO_SALVO ORDER BY ID_CAMINHO");
    }

    // Buscar por ID
    public CaminhoSalvo buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<CaminhoSalvo> lista = consultar("SELECT " + COLUNAS + " FROM CAMINHO_SALVO WHERE ID_CAMINHO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_CAMINHO), 0) + 1 FROM CAMINHO_SALVO";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<CaminhoSalvo> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<CaminhoSalvo> lista = new ArrayList<CaminhoSalvo>();
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

    private CaminhoSalvo montar(ResultSet rs) throws SQLException {
        CaminhoSalvo caminhoSalvo = new CaminhoSalvo();
        caminhoSalvo.setIdCaminho(rs.getInt("ID_CAMINHO"));
        caminhoSalvo.setIdUsuario(rs.getInt("ID_USUARIO"));
        caminhoSalvo.setNome(rs.getString("NOME"));
        caminhoSalvo.setIdNoOrigem(rs.getInt("ID_NO_ORIGEM"));
        caminhoSalvo.setIdNoDestino(rs.getInt("ID_NO_DESTINO"));
        caminhoSalvo.setDtCriacao(JdbcUtil.getDataHora(rs, "DT_CRIACAO"));
        return caminhoSalvo;
    }

    // Caminhos salvos de um usuário
    public List<CaminhoSalvo> selecionarPorUsuario(int idUsuario) throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM CAMINHO_SALVO WHERE ID_USUARIO = ? ORDER BY NOME", idUsuario);
    }
}
