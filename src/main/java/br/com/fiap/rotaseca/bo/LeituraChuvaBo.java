package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.LeituraChuvaDAO;
import br.com.fiap.rotaseca.entities.LeituraChuva;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de LeituraChuva.
 */
public class LeituraChuvaBo {

    LeituraChuvaDAO leituraChuvaDAO = new LeituraChuvaDAO();

    // Selecionar
    public ArrayList<LeituraChuva> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<LeituraChuva>) leituraChuvaDAO.selecionar();
    }

    // Buscar por ID
    public LeituraChuva buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        LeituraChuva leituraChuva = leituraChuvaDAO.buscarPorId(id);
        if (leituraChuva == null) {
            throw new RecursoNaoEncontradoException("LeituraChuva " + id + " não encontrado");
        }
        return leituraChuva;
    }

    // Inserir
    public LeituraChuva inserirBo(LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        Validacao.corpo(leituraChuva);
        // Regra de negócio
        validar(leituraChuva);
        if (leituraChuva.getIdLeitura() <= 0) {
            leituraChuva.setIdLeitura(leituraChuvaDAO.proximoId());
        }
        leituraChuvaDAO.inserir(leituraChuva);
        return leituraChuva;
    }

    // Atualizar
    public LeituraChuva atualizarBo(int id, LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        Validacao.corpo(leituraChuva);
        leituraChuva.setIdLeitura(id);
        // Regra de negócio
        validar(leituraChuva);
        if (!leituraChuvaDAO.atualizar(leituraChuva)) {
            throw new RecursoNaoEncontradoException("LeituraChuva " + id + " não encontrado");
        }
        return leituraChuva;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!leituraChuvaDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("LeituraChuva " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        if (leituraChuva.getDtLeitura() == null)
            throw new RegraNegocioException("O campo dtLeitura é obrigatório");
        if (leituraChuva.getPrecipitacaoMm() < 0)
            throw new RegraNegocioException("precipitacaoMm não pode ser negativa");
        if (Validacao.vazio(leituraChuva.getFonte())) leituraChuva.setFonte("SIMULADO");
        Validacao.dominio(leituraChuva.getFonte(), "fonte", "CEMADEN", "INMET", "SIMULADO");
    }

    public ArrayList<LeituraChuva> selecionarPorBairroBo(int idBairro) throws SQLException, ExcecoesConexao {
        new BairroBo().buscarPorIdBo(idBairro);
        return (ArrayList<LeituraChuva>) leituraChuvaDAO.selecionarPorBairro(idBairro);
    }
}
