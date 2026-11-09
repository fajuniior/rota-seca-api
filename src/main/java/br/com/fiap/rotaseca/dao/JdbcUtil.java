package br.com.fiap.rotaseca.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

/**
 * Funções de apoio para tratar colunas que aceitam NULL e datas no JDBC.
 */
public final class JdbcUtil {

    private JdbcUtil() {
    }

    public static void setInteger(PreparedStatement stmt, int indice, Integer valor) throws SQLException {
        if (valor == null) stmt.setNull(indice, Types.NUMERIC);
        else stmt.setInt(indice, valor);
    }

    public static void setDouble(PreparedStatement stmt, int indice, Double valor) throws SQLException {
        if (valor == null) stmt.setNull(indice, Types.NUMERIC);
        else stmt.setDouble(indice, valor);
    }

    public static void setDataHora(PreparedStatement stmt, int indice, LocalDateTime valor) throws SQLException {
        if (valor == null) stmt.setNull(indice, Types.TIMESTAMP);
        else stmt.setTimestamp(indice, Timestamp.valueOf(valor));
    }

    public static Integer getInteger(ResultSet rs, String coluna) throws SQLException {
        int valor = rs.getInt(coluna);
        return rs.wasNull() ? null : valor;
    }

    public static Double getDouble(ResultSet rs, String coluna) throws SQLException {
        double valor = rs.getDouble(coluna);
        return rs.wasNull() ? null : valor;
    }

    public static LocalDateTime getDataHora(ResultSet rs, String coluna) throws SQLException {
        Timestamp valor = rs.getTimestamp(coluna);
        return valor == null ? null : valor.toLocalDateTime();
    }

    // Preenche os parâmetros "?" de uma consulta, na ordem
    public static void preencher(PreparedStatement stmt, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            Object p = parametros[i];
            if (p == null) stmt.setNull(i + 1, Types.VARCHAR);
            else if (p instanceof Integer v) stmt.setInt(i + 1, v);
            else if (p instanceof Long v) stmt.setLong(i + 1, v);
            else if (p instanceof Timestamp v) stmt.setTimestamp(i + 1, v);
            else stmt.setString(i + 1, p.toString());
        }
    }
}
