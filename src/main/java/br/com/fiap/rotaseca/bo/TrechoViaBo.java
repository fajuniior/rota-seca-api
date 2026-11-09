package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.TrechoViaDAO;
import br.com.fiap.rotaseca.entities.TrechoVia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de TrechoVia.
 */
public class TrechoViaBo {

    TrechoViaDAO trechoViaDAO = new TrechoViaDAO();

    // Selecionar
    public ArrayList<TrechoVia> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<TrechoVia>) trechoViaDAO.selecionar();
    }

    // Buscar por ID
    public TrechoVia buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        TrechoVia trechoVia = trechoViaDAO.buscarPorId(id);
        if (trechoVia == null) {
            throw new RecursoNaoEncontradoException("TrechoVia " + id + " não encontrado");
        }
        return trechoVia;
    }

    // Inserir
    public TrechoVia inserirBo(TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        Validacao.corpo(trechoVia);
        // Regra de negócio
        validar(trechoVia);
        if (trechoVia.getIdTrecho() <= 0) {
            trechoVia.setIdTrecho(trechoViaDAO.proximoId());
        }
        trechoViaDAO.inserir(trechoVia);
        return trechoVia;
    }

    // Atualizar
    public TrechoVia atualizarBo(int id, TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        Validacao.corpo(trechoVia);
        trechoVia.setIdTrecho(id);
        // Regra de negócio
        validar(trechoVia);
        if (!trechoViaDAO.atualizar(trechoVia)) {
            throw new RecursoNaoEncontradoException("TrechoVia " + id + " não encontrado");
        }
        return trechoVia;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!trechoViaDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("TrechoVia " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(trechoVia.getNomeVia(), "nomeVia");
        if (trechoVia.getIdNoOrigem() == trechoVia.getIdNoDestino())
            throw new RegraNegocioException("O trecho precisa ligar dois nós diferentes");
        if (trechoVia.getDistanciaM() <= 0)
            throw new RegraNegocioException("distanciaM deve ser maior que zero");
        if (trechoVia.getNivelRisco() < 0 || trechoVia.getNivelRisco() > 5)
            throw new RegraNegocioException("nivelRisco deve estar entre 0 e 5");
        if (Validacao.vazio(trechoVia.getStTrecho())) trechoVia.setStTrecho("ATIVO");
        Validacao.dominio(trechoVia.getStTrecho(), "stTrecho", "ATIVO", "INTERDITADO");
    }
}
