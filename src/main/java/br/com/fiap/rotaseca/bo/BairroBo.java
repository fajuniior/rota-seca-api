package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.BairroDAO;
import br.com.fiap.rotaseca.entities.Bairro;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de Bairro.
 */
public class BairroBo {

    BairroDAO bairroDAO = new BairroDAO();

    // Selecionar
    public ArrayList<Bairro> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Bairro>) bairroDAO.selecionar();
    }

    // Buscar por ID
    public Bairro buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        Bairro bairro = bairroDAO.buscarPorId(id);
        if (bairro == null) {
            throw new RecursoNaoEncontradoException("Bairro " + id + " não encontrado");
        }
        return bairro;
    }

    // Inserir
    public Bairro inserirBo(Bairro bairro) throws SQLException, ExcecoesConexao {
        Validacao.corpo(bairro);
        // Regra de negócio
        validar(bairro);
        if (bairro.getIdBairro() <= 0) {
            bairro.setIdBairro(bairroDAO.proximoId());
        }
        bairroDAO.inserir(bairro);
        return bairro;
    }

    // Atualizar
    public Bairro atualizarBo(int id, Bairro bairro) throws SQLException, ExcecoesConexao {
        Validacao.corpo(bairro);
        bairro.setIdBairro(id);
        // Regra de negócio
        validar(bairro);
        if (!bairroDAO.atualizar(bairro)) {
            throw new RecursoNaoEncontradoException("Bairro " + id + " não encontrado");
        }
        return bairro;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!bairroDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("Bairro " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(Bairro bairro) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(bairro.getNome(), "nome");
        if (Validacao.vazio(bairro.getCidade())) bairro.setCidade("Guarulhos");
    }
}
