package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.ContatoConfianca;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela CONTATO_CONFIANCA.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class ContatoConfiancaDAO {

    private static final String COLUNAS = "ID_CONTATO, ID_USUARIO, NOME, TELEFONE, PARENTESCO, ST_RECEBE_GRAVE";

    // Insert
    public String inserir(ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO CONTATO_CONFIANCA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contatoConfianca.getIdContato());
            stmt.setInt(2, contatoConfianca.getIdUsuario());
            stmt.setString(3, contatoConfianca.getNome());
            stmt.setString(4, contatoConfianca.getTelefone());
            stmt.setString(5, contatoConfianca.getParentesco());
            stmt.setString(6, contatoConfianca.getStRecebeGrave());
            stmt.execute();
        }
        return "ContatoConfianca cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM CONTATO_CONFIANCA WHERE ID_CONTATO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE CONTATO_CONFIANCA SET ID_USUARIO = ?, NOME = ?, TELEFONE = ?, PARENTESCO = ?, ST_RECEBE_GRAVE = ? WHERE ID_CONTATO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contatoConfianca.getIdUsuario());
            stmt.setString(2, contatoConfianca.getNome());
            stmt.setString(3, contatoConfianca.getTelefone());
            stmt.setString(4, contatoConfianca.getParentesco());
            stmt.setString(5, contatoConfianca.getStRecebeGrave());
            stmt.setInt(6, contatoConfianca.getIdContato());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<ContatoConfianca> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM CONTATO_CONFIANCA ORDER BY ID_CONTATO");
    }

    // Buscar por ID
    public ContatoConfianca buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<ContatoConfianca> lista = consultar("SELECT " + COLUNAS + " FROM CONTATO_CONFIANCA WHERE ID_CONTATO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_CONTATO), 0) + 1 FROM CONTATO_CONFIANCA";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<ContatoConfianca> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<ContatoConfianca> lista = new ArrayList<ContatoConfianca>();
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

    private ContatoConfianca montar(ResultSet rs) throws SQLException {
        ContatoConfianca contatoConfianca = new ContatoConfianca();
        contatoConfianca.setIdContato(rs.getInt("ID_CONTATO"));
        contatoConfianca.setIdUsuario(rs.getInt("ID_USUARIO"));
        contatoConfianca.setNome(rs.getString("NOME"));
        contatoConfianca.setTelefone(rs.getString("TELEFONE"));
        contatoConfianca.setParentesco(rs.getString("PARENTESCO"));
        contatoConfianca.setStRecebeGrave(rs.getString("ST_RECEBE_GRAVE"));
        return contatoConfianca;
    }

    // Contatos de confiança de um usuário
    public List<ContatoConfianca> selecionarPorUsuario(int idUsuario) throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM CONTATO_CONFIANCA WHERE ID_USUARIO = ? ORDER BY NOME", idUsuario);
    }
}
