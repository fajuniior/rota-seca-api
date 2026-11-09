package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.CaminhoTrechoDAO;
import br.com.fiap.rotaseca.entities.CaminhoTrecho;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio dos trechos de um caminho salvo.
 */
public class CaminhoTrechoBo {

    CaminhoTrechoDAO caminhoTrechoDAO = new CaminhoTrechoDAO();

    public ArrayList<CaminhoTrecho> selecionarPorCaminhoBo(int idCaminho) throws SQLException, ExcecoesConexao {
        new CaminhoSalvoBo().buscarPorIdBo(idCaminho);
        return (ArrayList<CaminhoTrecho>) caminhoTrechoDAO.selecionarPorCaminho(idCaminho);
    }

    public CaminhoTrecho inserirBo(int idCaminho, CaminhoTrecho caminhoTrecho) throws SQLException, ExcecoesConexao {
        Validacao.corpo(caminhoTrecho);
        caminhoTrecho.setIdCaminho(idCaminho);
        if (caminhoTrecho.getNrOrdem() <= 0) {
            // Regra: sem ordem informada, o trecho entra no fim do caminho
            caminhoTrecho.setNrOrdem(selecionarPorCaminhoBo(idCaminho).size() + 1);
        }
        caminhoTrechoDAO.inserir(caminhoTrecho);
        return caminhoTrecho;
    }

    public CaminhoTrecho atualizarBo(int idCaminho, int nrOrdem, CaminhoTrecho caminhoTrecho) throws SQLException, ExcecoesConexao {
        Validacao.corpo(caminhoTrecho);
        if (caminhoTrecho.getIdTrecho() <= 0) throw new RegraNegocioException("O campo idTrecho é obrigatório");
        caminhoTrecho.setIdCaminho(idCaminho);
        caminhoTrecho.setNrOrdem(nrOrdem);
        if (!caminhoTrechoDAO.atualizar(caminhoTrecho)) {
            throw new RecursoNaoEncontradoException("Posição " + nrOrdem + " do caminho " + idCaminho + " não encontrada");
        }
        return caminhoTrecho;
    }

    public void deletarBo(int idCaminho, int nrOrdem) throws SQLException, ExcecoesConexao {
        if (!caminhoTrechoDAO.deletar(idCaminho, nrOrdem)) {
            throw new RecursoNaoEncontradoException("Posição " + nrOrdem + " do caminho " + idCaminho + " não encontrada");
        }
    }
}
