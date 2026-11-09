package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.CaminhoSalvoDAO;
import br.com.fiap.rotaseca.entities.CaminhoSalvo;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de CaminhoSalvo.
 */
public class CaminhoSalvoBo {

    CaminhoSalvoDAO caminhoSalvoDAO = new CaminhoSalvoDAO();

    // Selecionar
    public ArrayList<CaminhoSalvo> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<CaminhoSalvo>) caminhoSalvoDAO.selecionar();
    }

    // Buscar por ID
    public CaminhoSalvo buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        CaminhoSalvo caminhoSalvo = caminhoSalvoDAO.buscarPorId(id);
        if (caminhoSalvo == null) {
            throw new RecursoNaoEncontradoException("CaminhoSalvo " + id + " não encontrado");
        }
        return caminhoSalvo;
    }

    // Inserir
    public CaminhoSalvo inserirBo(CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        Validacao.corpo(caminhoSalvo);
        // Regra de negócio
        validar(caminhoSalvo);
        if (caminhoSalvo.getIdCaminho() <= 0) {
            caminhoSalvo.setIdCaminho(caminhoSalvoDAO.proximoId());
        }
        caminhoSalvoDAO.inserir(caminhoSalvo);
        return caminhoSalvo;
    }

    // Atualizar
    public CaminhoSalvo atualizarBo(int id, CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        Validacao.corpo(caminhoSalvo);
        caminhoSalvo.setIdCaminho(id);
        // Regra de negócio
        validar(caminhoSalvo);
        if (!caminhoSalvoDAO.atualizar(caminhoSalvo)) {
            throw new RecursoNaoEncontradoException("CaminhoSalvo " + id + " não encontrado");
        }
        return caminhoSalvo;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!caminhoSalvoDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("CaminhoSalvo " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(caminhoSalvo.getNome(), "nome");
        if (caminhoSalvo.getIdNoOrigem() == caminhoSalvo.getIdNoDestino())
            throw new RegraNegocioException("Origem e destino do caminho precisam ser diferentes");
        if (caminhoSalvo.getDtCriacao() == null) caminhoSalvo.setDtCriacao(Relogio.agora());
    }

    public ArrayList<CaminhoSalvo> selecionarPorUsuarioBo(int idUsuario) throws SQLException, ExcecoesConexao {
        new UsuarioBo().buscarPorIdBo(idUsuario);
        return (ArrayList<CaminhoSalvo>) caminhoSalvoDAO.selecionarPorUsuario(idUsuario);
    }
}
