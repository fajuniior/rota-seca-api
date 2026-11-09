package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.BairroAcompanhado;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela BAIRRO_ACOMPANHADO (chave composta: ID_USUARIO + ID_BAIRRO).
 */
public class BairroAcompanhadoDAO {

    // Insert
    public String inserir(BairroAcompanhado acompanhado) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO BAIRRO_ACOMPANHADO (ID_USUARIO, ID_BAIRRO, MOTIVO, DT_INICIO) VALUES (?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, acompanhado.getIdUsuario());
            stmt.setInt(2, acompanhado.getIdBairro());
            stmt.setString(3, acompanhado.getMotivo());
            JdbcUtil.setDataHora(stmt, 4, acompanhado.getDtInicio());
            stmt.execute();
        }
        return "Bairro acompanhado com sucesso!";
    }

    // Update: altera o motivo do acompanhamento
    public boolean atualizar(BairroAcompanhado acompanhado) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE BAIRRO_ACOMPANHADO SET MOTIVO = ? WHERE ID_USUARIO = ? AND ID_BAIRRO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, acompanhado.getMotivo());
            stmt.setInt(2, acompanhado.getIdUsuario());
            stmt.setInt(3, acompanhado.getIdBairro());
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete
    public boolean deletar(int idUsuario, int idBairro) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM BAIRRO_ACOMPANHADO WHERE ID_USUARIO = ? AND ID_BAIRRO = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idBairro);
            return stmt.executeUpdate() > 0;
        }
    }

    // Select: bairros acompanhados por um usuário
    public List<BairroAcompanhado> selecionarPorUsuario(int idUsuario) throws SQLException, ExcecoesConexao {
        String sql = "SELECT ID_USUARIO, ID_BAIRRO, MOTIVO, DT_INICIO FROM BAIRRO_ACOMPANHADO WHERE ID_USUARIO = ? ORDER BY ID_BAIRRO";
        List<BairroAcompanhado> lista = new ArrayList<BairroAcompanhado>();
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new BairroAcompanhado(rs.getInt("ID_USUARIO"), rs.getInt("ID_BAIRRO"),
                            rs.getString("MOTIVO"), JdbcUtil.getDataHora(rs, "DT_INICIO")));
                }
            }
        }
        return lista;
    }
}
