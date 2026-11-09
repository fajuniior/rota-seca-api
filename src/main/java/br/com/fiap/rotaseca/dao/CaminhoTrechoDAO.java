package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.CaminhoTrecho;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela CAMINHO_TRECHO (chave composta: ID_CAMINHO + NR_ORDEM).
 */
public class CaminhoTrechoDAO {

    // Insert
    public String inserir(CaminhoTrecho caminhoTrecho) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO CAMINHO_TRECHO (ID_CAMINHO, NR_ORDEM, ID_TRECHO) VALUES (?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, caminhoTrecho.getIdCaminho());
            stmt.setInt(2, caminhoTrecho.getNrOrdem());
            stmt.setInt(3, caminhoTrecho.getIdTrecho());
            stmt.execute();
        }
        return "Trecho adicionado ao caminho!";
    }

    // Update: troca o trecho que ocupa uma posição do caminho
    public boolean atualizar(CaminhoTrecho caminhoTrecho) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE CAMINHO_TRECHO SET ID_TRECHO = ? WHERE ID_CAMINHO = ? AND NR_ORDEM = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, caminhoTrecho.getIdTrecho());
            stmt.setInt(2, caminhoTrecho.getIdCaminho());
            stmt.setInt(3, caminhoTrecho.getNrOrdem());
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete
    public boolean deletar(int idCaminho, int nrOrdem) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM CAMINHO_TRECHO WHERE ID_CAMINHO = ? AND NR_ORDEM = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idCaminho);
            stmt.setInt(2, nrOrdem);
            return stmt.executeUpdate() > 0;
        }
    }

    // Select: trechos de um caminho, em ordem
    public List<CaminhoTrecho> selecionarPorCaminho(int idCaminho) throws SQLException, ExcecoesConexao {
        String sql = "SELECT ID_CAMINHO, NR_ORDEM, ID_TRECHO FROM CAMINHO_TRECHO WHERE ID_CAMINHO = ? ORDER BY NR_ORDEM";
        List<CaminhoTrecho> lista = new ArrayList<CaminhoTrecho>();
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idCaminho);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CaminhoTrecho(rs.getInt("ID_CAMINHO"), rs.getInt("NR_ORDEM"), rs.getInt("ID_TRECHO")));
                }
            }
        }
        return lista;
    }
}
