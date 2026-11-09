package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.BairroAcompanhadoDAO;
import br.com.fiap.rotaseca.entities.BairroAcompanhado;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio dos bairros acompanhados (US23: acompanhar o bairro de um familiar).
 */
public class BairroAcompanhadoBo {

    BairroAcompanhadoDAO bairroAcompanhadoDAO = new BairroAcompanhadoDAO();

    public ArrayList<BairroAcompanhado> selecionarPorUsuarioBo(int idUsuario) throws SQLException, ExcecoesConexao {
        new UsuarioBo().buscarPorIdBo(idUsuario);
        return (ArrayList<BairroAcompanhado>) bairroAcompanhadoDAO.selecionarPorUsuario(idUsuario);
    }

    public BairroAcompanhado inserirBo(int idUsuario, BairroAcompanhado acompanhado) throws SQLException, ExcecoesConexao {
        Validacao.corpo(acompanhado);
        acompanhado.setIdUsuario(idUsuario);
        new BairroBo().buscarPorIdBo(acompanhado.getIdBairro());
        if (acompanhado.getDtInicio() == null) acompanhado.setDtInicio(Relogio.agora());
        bairroAcompanhadoDAO.inserir(acompanhado);
        return acompanhado;
    }

    public BairroAcompanhado atualizarBo(int idUsuario, int idBairro, BairroAcompanhado acompanhado) throws SQLException, ExcecoesConexao {
        Validacao.corpo(acompanhado);
        acompanhado.setIdUsuario(idUsuario);
        acompanhado.setIdBairro(idBairro);
        if (!bairroAcompanhadoDAO.atualizar(acompanhado)) {
            throw new RecursoNaoEncontradoException("O usuário " + idUsuario + " não acompanha o bairro " + idBairro);
        }
        return acompanhado;
    }

    public void deletarBo(int idUsuario, int idBairro) throws SQLException, ExcecoesConexao {
        if (!bairroAcompanhadoDAO.deletar(idUsuario, idBairro)) {
            throw new RecursoNaoEncontradoException("O usuário " + idUsuario + " não acompanha o bairro " + idBairro);
        }
    }
}
