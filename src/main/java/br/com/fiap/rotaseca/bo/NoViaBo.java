package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.NoViaDAO;
import br.com.fiap.rotaseca.entities.NoVia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de NoVia.
 */
public class NoViaBo {

    NoViaDAO noViaDAO = new NoViaDAO();

    // Selecionar
    public ArrayList<NoVia> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<NoVia>) noViaDAO.selecionar();
    }

    // Buscar por ID
    public NoVia buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        NoVia noVia = noViaDAO.buscarPorId(id);
        if (noVia == null) {
            throw new RecursoNaoEncontradoException("NoVia " + id + " não encontrado");
        }
        return noVia;
    }

    // Inserir
    public NoVia inserirBo(NoVia noVia) throws SQLException, ExcecoesConexao {
        Validacao.corpo(noVia);
        // Regra de negócio
        validar(noVia);
        if (noVia.getIdNo() <= 0) {
            noVia.setIdNo(noViaDAO.proximoId());
        }
        noViaDAO.inserir(noVia);
        return noVia;
    }

    // Atualizar
    public NoVia atualizarBo(int id, NoVia noVia) throws SQLException, ExcecoesConexao {
        Validacao.corpo(noVia);
        noVia.setIdNo(id);
        // Regra de negócio
        validar(noVia);
        if (!noViaDAO.atualizar(noVia)) {
            throw new RecursoNaoEncontradoException("NoVia " + id + " não encontrado");
        }
        return noVia;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!noViaDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("NoVia " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(NoVia noVia) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(noVia.getReferencia(), "referencia");
        if (noVia.getLatitude() < -90 || noVia.getLatitude() > 90)
            throw new RegraNegocioException("latitude deve estar entre -90 e 90");
        if (noVia.getLongitude() < -180 || noVia.getLongitude() > 180)
            throw new RegraNegocioException("longitude deve estar entre -180 e 180");
    }
}
