package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.ConfirmacaoDAO;
import br.com.fiap.rotaseca.entities.Confirmacao;
import br.com.fiap.rotaseca.entities.Ocorrencia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de Confirmacao (US06).
 */
public class ConfirmacaoBo {

    ConfirmacaoDAO confirmacaoDAO = new ConfirmacaoDAO();

    // Selecionar
    public ArrayList<Confirmacao> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Confirmacao>) confirmacaoDAO.selecionar();
    }

    // Confirmações de uma ocorrência
    public ArrayList<Confirmacao> selecionarPorOcorrenciaBo(int idOcorrencia) throws SQLException, ExcecoesConexao {
        new OcorrenciaBo().buscarPorIdBo(idOcorrencia);
        return (ArrayList<Confirmacao>) confirmacaoDAO.selecionarPorOcorrencia(idOcorrencia);
    }

    // Buscar por ID
    public Confirmacao buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        Confirmacao confirmacao = confirmacaoDAO.buscarPorId(id);
        if (confirmacao == null) {
            throw new RecursoNaoEncontradoException("Confirmacao " + id + " não encontrada");
        }
        return confirmacao;
    }

    // Inserir
    public Confirmacao inserirBo(Confirmacao confirmacao) throws SQLException, ExcecoesConexao {
        Validacao.corpo(confirmacao);
        Ocorrencia ocorrencia = new OcorrenciaBo().buscarPorIdBo(confirmacao.getIdOcorrencia());
        // Regras de negócio
        if (ocorrencia.getIdUsuario() == confirmacao.getIdUsuario()) {
            throw new RegraNegocioException("O autor não pode confirmar a própria ocorrência");
        }
        if ("DESCARTADA".equals(ocorrencia.getStValidacao())) {
            throw new RegraNegocioException("Ocorrência descartada pela Defesa Civil não pode ser confirmada");
        }
        if (ocorrencia.getDtExpiracao() != null && !ocorrencia.getDtExpiracao().isAfter(Relogio.agora())) {
            throw new RegraNegocioException("Ocorrência expirada: registre uma nova ocorrência");
        }
        confirmacao.setDtConfirmacao(Relogio.agora());
        if (confirmacao.getIdConfirmacao() <= 0) {
            confirmacao.setIdConfirmacao(confirmacaoDAO.proximoId());
        }
        confirmacaoDAO.inserir(confirmacao);
        return confirmacao;
    }

    // Atualizar
    public Confirmacao atualizarBo(int id, Confirmacao confirmacao) throws SQLException, ExcecoesConexao {
        Validacao.corpo(confirmacao);
        confirmacao.setIdConfirmacao(id);
        if (confirmacao.getDtConfirmacao() == null) confirmacao.setDtConfirmacao(Relogio.agora());
        if (!confirmacaoDAO.atualizar(confirmacao)) {
            throw new RecursoNaoEncontradoException("Confirmacao " + id + " não encontrada");
        }
        return confirmacao;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!confirmacaoDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("Confirmacao " + id + " não encontrada");
        }
    }
}
