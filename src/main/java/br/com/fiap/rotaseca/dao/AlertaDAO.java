package br.com.fiap.rotaseca.dao;

import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.entities.Alerta;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela ALERTA.
 * Cada método abre a conexão e a fecha ao final (try-with-resources),
 * para não deixar conexões presas no servidor Oracle.
 */
public class AlertaDAO {

    private static final String COLUNAS = "ID_ALERTA, ID_BAIRRO, NIVEL, MENSAGEM, PROB_CHUVA_INTENSA, ORIGEM, ID_AGENTE, DT_INICIO, DT_FIM";

    // Insert
    public String inserir(Alerta alerta) throws SQLException, ExcecoesConexao {
        String sql = "INSERT INTO ALERTA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, alerta.getIdAlerta());
            stmt.setInt(2, alerta.getIdBairro());
            stmt.setString(3, alerta.getNivel());
            stmt.setString(4, alerta.getMensagem());
            JdbcUtil.setDouble(stmt, 5, alerta.getProbChuvaIntensa());
            stmt.setString(6, alerta.getOrigem());
            JdbcUtil.setInteger(stmt, 7, alerta.getIdAgente());
            JdbcUtil.setDataHora(stmt, 8, alerta.getDtInicio());
            JdbcUtil.setDataHora(stmt, 9, alerta.getDtFim());
            stmt.execute();
        }
        return "Alerta cadastrado com sucesso!";
    }

    // Delete
    public boolean deletar(int id) throws SQLException, ExcecoesConexao {
        String sql = "DELETE FROM ALERTA WHERE ID_ALERTA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Update
    public boolean atualizar(Alerta alerta) throws SQLException, ExcecoesConexao {
        String sql = "UPDATE ALERTA SET ID_BAIRRO = ?, NIVEL = ?, MENSAGEM = ?, PROB_CHUVA_INTENSA = ?, ORIGEM = ?, ID_AGENTE = ?, DT_INICIO = ?, DT_FIM = ? WHERE ID_ALERTA = ?";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, alerta.getIdBairro());
            stmt.setString(2, alerta.getNivel());
            stmt.setString(3, alerta.getMensagem());
            JdbcUtil.setDouble(stmt, 4, alerta.getProbChuvaIntensa());
            stmt.setString(5, alerta.getOrigem());
            JdbcUtil.setInteger(stmt, 6, alerta.getIdAgente());
            JdbcUtil.setDataHora(stmt, 7, alerta.getDtInicio());
            JdbcUtil.setDataHora(stmt, 8, alerta.getDtFim());
            stmt.setInt(9, alerta.getIdAlerta());
            return stmt.executeUpdate() > 0;
        }
    }

    // Select
    public List<Alerta> selecionar() throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM ALERTA ORDER BY ID_ALERTA");
    }

    // Buscar por ID
    public Alerta buscarPorId(int id) throws SQLException, ExcecoesConexao {
        List<Alerta> lista = consultar("SELECT " + COLUNAS + " FROM ALERTA WHERE ID_ALERTA = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Próximo ID livre (a tabela não usa sequence)
    public int proximoId() throws SQLException, ExcecoesConexao {
        String sql = "SELECT NVL(MAX(ID_ALERTA), 0) + 1 FROM ALERTA";
        try (Connection conexao = new ConexaoFactory().conexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    // Executa um SELECT com parâmetros e transforma cada linha em objeto
    protected List<Alerta> consultar(String sql, Object... parametros) throws SQLException, ExcecoesConexao {
        List<Alerta> lista = new ArrayList<Alerta>();
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

    private Alerta montar(ResultSet rs) throws SQLException {
        Alerta alerta = new Alerta();
        alerta.setIdAlerta(rs.getInt("ID_ALERTA"));
        alerta.setIdBairro(rs.getInt("ID_BAIRRO"));
        alerta.setNivel(rs.getString("NIVEL"));
        alerta.setMensagem(rs.getString("MENSAGEM"));
        alerta.setProbChuvaIntensa(JdbcUtil.getDouble(rs, "PROB_CHUVA_INTENSA"));
        alerta.setOrigem(rs.getString("ORIGEM"));
        alerta.setIdAgente(JdbcUtil.getInteger(rs, "ID_AGENTE"));
        alerta.setDtInicio(JdbcUtil.getDataHora(rs, "DT_INICIO"));
        alerta.setDtFim(JdbcUtil.getDataHora(rs, "DT_FIM"));
        return alerta;
    }

    // Alerta vigente de um bairro (o mais recente ainda sem data de fim ou com fim no futuro)
    public Alerta buscarVigentePorBairro(int idBairro, java.time.LocalDateTime agora) throws SQLException, ExcecoesConexao {
        List<Alerta> lista = consultar("SELECT " + COLUNAS + " FROM ALERTA WHERE ID_BAIRRO = ? AND DT_INICIO <= ? "
                + "AND (DT_FIM IS NULL OR DT_FIM > ?) ORDER BY DT_INICIO DESC",
                idBairro, java.sql.Timestamp.valueOf(agora), java.sql.Timestamp.valueOf(agora));
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Histórico de alertas de um bairro
    public List<Alerta> selecionarPorBairro(int idBairro) throws SQLException, ExcecoesConexao {
        return consultar("SELECT " + COLUNAS + " FROM ALERTA WHERE ID_BAIRRO = ? ORDER BY DT_INICIO DESC", idBairro);
    }
}
