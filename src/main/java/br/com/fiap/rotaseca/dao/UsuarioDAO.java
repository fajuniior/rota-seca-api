package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.Usuario;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela USUARIO.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class UsuarioDAO {

    private static final String COLUNAS = "ID_USUARIO, NOME_EXIBICAO, TELEFONE, TIPO_PERFIL, ID_BAIRRO, DT_CADASTRO, ST_ATIVO";

    // Insert
    public String inserir(Usuario usuario) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO USUARIO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNomeExibicao());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getTipoPerfil());
            JdbcUtil.setInteger(stmt, 5, usuario.getIdBairro());
            JdbcUtil.setDataHora(stmt, 6, usuario.getDtCadastro());
            stmt.setString(7, usuario.getStAtivo());
            stmt.execute();
        }
        return "Usuario cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(Usuario usuario) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE USUARIO SET NOME_EXIBICAO = ?, TELEFONE = ?, TIPO_PERFIL = ?, ID_BAIRRO = ?, DT_CADASTRO = ?, ST_ATIVO = ? WHERE ID_USUARIO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNomeExibicao());
            stmt.setString(2, usuario.getTelefone());
            stmt.setString(3, usuario.getTipoPerfil());
            JdbcUtil.setInteger(stmt, 4, usuario.getIdBairro());
            JdbcUtil.setDataHora(stmt, 5, usuario.getDtCadastro());
            stmt.setString(6, usuario.getStAtivo());
            stmt.setInt(7, usuario.getIdUsuario());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<Usuario> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM USUARIO ORDER BY ID_USUARIO");
    }

    // Buscar por ID
    public Usuario buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<Usuario> lista = consultar("SELECT " + COLUNAS + " FROM USUARIO WHERE ID_USUARIO = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_USUARIO), 0) + 1 FROM USUARIO";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<Usuario> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<Usuario> lista = new ArrayList<Usuario>();
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

    private Usuario montar(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
        usuario.setNomeExibicao(rs.getString("NOME_EXIBICAO"));
        usuario.setTelefone(rs.getString("TELEFONE"));
        usuario.setTipoPerfil(rs.getString("TIPO_PERFIL"));
        usuario.setIdBairro(JdbcUtil.getInteger(rs, "ID_BAIRRO"));
        usuario.setDtCadastro(JdbcUtil.getDataHora(rs, "DT_CADASTRO"));
        usuario.setStAtivo(rs.getString("ST_ATIVO"));
        return usuario;
    }
}
