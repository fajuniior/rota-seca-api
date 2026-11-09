package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.dto.AvisoCaminho;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Consulta "quem avisar": ocorrências ativas em trechos que fazem parte
 * dos caminhos salvos de um usuário (US02 do backlog).
 */
public class AvisoCaminhoDAO {

    public List<AvisoCaminho> selecionarPorUsuario(int idUsuario, LocalDateTime agora)
            throws SQLException, ExcecoesConexao {
        String sql = "SELECT CS.ID_CAMINHO, CS.NOME, T.ID_TRECHO, T.NOME_VIA, O.ID_OCORRENCIA, O.TIPO, "
                + "O.ST_VALIDACAO, O.DT_REGISTRO, "
                + "(SELECT COUNT(*) FROM CONFIRMACAO C WHERE C.ID_OCORRENCIA = O.ID_OCORRENCIA) AS CONFIRMACOES "
                + "FROM CAMINHO_SALVO CS "
                + "INNER JOIN CAMINHO_TRECHO CT ON CS.ID_CAMINHO = CT.ID_CAMINHO "
                + "INNER JOIN TRECHO_VIA T ON CT.ID_TRECHO = T.ID_TRECHO "
                + "INNER JOIN OCORRENCIA O ON T.ID_TRECHO = O.ID_TRECHO "
                + "WHERE CS.ID_USUARIO = ? AND O.ST_VALIDACAO <> 'DESCARTADA' "
                + "AND O.DT_REGISTRO <= ? AND O.DT_EXPIRACAO > ? "
                + "ORDER BY CS.NOME, CT.NR_ORDEM";
        List<AvisoCaminho> lista = new ArrayList<AvisoCaminho>();
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setTimestamp(2, Timestamp.valueOf(agora));
            stmt.setTimestamp(3, Timestamp.valueOf(agora));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AvisoCaminho aviso = new AvisoCaminho();
                    aviso.setIdCaminho(rs.getInt("ID_CAMINHO"));
                    aviso.setCaminho(rs.getString("NOME"));
                    aviso.setIdTrecho(rs.getInt("ID_TRECHO"));
                    aviso.setNomeVia(rs.getString("NOME_VIA"));
                    aviso.setIdOcorrencia(rs.getInt("ID_OCORRENCIA"));
                    aviso.setTipo(rs.getString("TIPO"));
                    aviso.setStValidacao(rs.getString("ST_VALIDACAO"));
                    aviso.setDtRegistro(JdbcUtil.getDataHora(rs, "DT_REGISTRO"));
                    aviso.setConfirmacoes(rs.getInt("CONFIRMACOES"));
                    lista.add(aviso);
                }
            }
        }
        return lista;
    }
}
