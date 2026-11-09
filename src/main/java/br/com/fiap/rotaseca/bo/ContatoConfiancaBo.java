package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.ContatoConfiancaDAO;
import br.com.fiap.rotaseca.entities.ContatoConfianca;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de ContatoConfianca.
 */
public class ContatoConfiancaBo {

    ContatoConfiancaDAO contatoConfiancaDAO = new ContatoConfiancaDAO();

    // Selecionar
    public ArrayList<ContatoConfianca> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<ContatoConfianca>) contatoConfiancaDAO.selecionar();
    }

    // Buscar por ID
    public ContatoConfianca buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        ContatoConfianca contatoConfianca = contatoConfiancaDAO.buscarPorId(id);
        if (contatoConfianca == null) {
            throw new RecursoNaoEncontradoException("ContatoConfianca " + id + " não encontrado");
        }
        return contatoConfianca;
    }

    // Inserir
    public ContatoConfianca inserirBo(ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        Validacao.corpo(contatoConfianca);
        // Regra de negócio
        validar(contatoConfianca);
        if (contatoConfianca.getIdContato() <= 0) {
            contatoConfianca.setIdContato(contatoConfiancaDAO.proximoId());
        }
        contatoConfiancaDAO.inserir(contatoConfianca);
        return contatoConfianca;
    }

    // Atualizar
    public ContatoConfianca atualizarBo(int id, ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        Validacao.corpo(contatoConfianca);
        contatoConfianca.setIdContato(id);
        // Regra de negócio
        validar(contatoConfianca);
        if (!contatoConfiancaDAO.atualizar(contatoConfianca)) {
            throw new RecursoNaoEncontradoException("ContatoConfianca " + id + " não encontrado");
        }
        return contatoConfianca;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!contatoConfiancaDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("ContatoConfianca " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(contatoConfianca.getNome(), "nome");
        Validacao.obrigatorio(contatoConfianca.getTelefone(), "telefone");
        if (Validacao.vazio(contatoConfianca.getStRecebeGrave())) contatoConfianca.setStRecebeGrave("S");
        Validacao.dominio(contatoConfianca.getStRecebeGrave(), "stRecebeGrave", "S", "N");
    }

    public ArrayList<ContatoConfianca> selecionarPorUsuarioBo(int idUsuario) throws SQLException, ExcecoesConexao {
        new UsuarioBo().buscarPorIdBo(idUsuario);
        return (ArrayList<ContatoConfianca>) contatoConfiancaDAO.selecionarPorUsuario(idUsuario);
    }
}
