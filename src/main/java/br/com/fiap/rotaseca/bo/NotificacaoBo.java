package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.NotificacaoDAO;
import br.com.fiap.rotaseca.entities.Notificacao;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de Notificacao.
 */
public class NotificacaoBo {

    NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    // Selecionar
    public ArrayList<Notificacao> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Notificacao>) notificacaoDAO.selecionar();
    }

    // Buscar por ID
    public Notificacao buscarPorIdBo(long id) throws SQLException, ExcecoesConexao {
        Notificacao notificacao = notificacaoDAO.buscarPorId(id);
        if (notificacao == null) {
            throw new RecursoNaoEncontradoException("Notificacao " + id + " não encontrado");
        }
        return notificacao;
    }

    // Inserir
    public Notificacao inserirBo(Notificacao notificacao) throws SQLException, ExcecoesConexao {
        Validacao.corpo(notificacao);
        // Regra de negócio
        validar(notificacao);
        if (notificacao.getIdNotificacao() <= 0) {
            notificacao.setIdNotificacao(notificacaoDAO.proximoId());
        }
        notificacaoDAO.inserir(notificacao);
        return notificacao;
    }

    // Atualizar
    public Notificacao atualizarBo(long id, Notificacao notificacao) throws SQLException, ExcecoesConexao {
        Validacao.corpo(notificacao);
        notificacao.setIdNotificacao(id);
        // Regra de negócio
        validar(notificacao);
        if (!notificacaoDAO.atualizar(notificacao)) {
            throw new RecursoNaoEncontradoException("Notificacao " + id + " não encontrado");
        }
        return notificacao;
    }

    // Deletar
    public void deletarBo(long id) throws SQLException, ExcecoesConexao {
        if (!notificacaoDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("Notificacao " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(Notificacao notificacao) throws SQLException, ExcecoesConexao {
        Validacao.dominio(notificacao.getCanal(), "canal", "PUSH", "SMS");
        if ((notificacao.getIdAlerta() == null) == (notificacao.getIdOcorrencia() == null))
            throw new RegraNegocioException("Informe exatamente uma origem: idAlerta ou idOcorrencia");
        if ((notificacao.getIdUsuario() == null) == (notificacao.getIdContato() == null))
            throw new RegraNegocioException("Informe exatamente um destinatário: idUsuario ou idContato");
        if (Validacao.vazio(notificacao.getStEntrega())) notificacao.setStEntrega("ENVIADA");
        Validacao.dominio(notificacao.getStEntrega(), "stEntrega", "ENVIADA", "ENTREGUE", "FALHOU");
        if (notificacao.getDtEnvio() == null) notificacao.setDtEnvio(Relogio.agora());
    }
}
