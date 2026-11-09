package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.PontoApoioDAO;
import br.com.fiap.rotaseca.entities.PontoApoio;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de PontoApoio.
 */
public class PontoApoioBo {

    PontoApoioDAO pontoApoioDAO = new PontoApoioDAO();

    // Selecionar
    public ArrayList<PontoApoio> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<PontoApoio>) pontoApoioDAO.selecionar();
    }

    // Buscar por ID
    public PontoApoio buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        PontoApoio pontoApoio = pontoApoioDAO.buscarPorId(id);
        if (pontoApoio == null) {
            throw new RecursoNaoEncontradoException("PontoApoio " + id + " não encontrado");
        }
        return pontoApoio;
    }

    // Inserir
    public PontoApoio inserirBo(PontoApoio pontoApoio) throws SQLException, ExcecoesConexao {
        Validacao.corpo(pontoApoio);
        // Regra de negócio
        validar(pontoApoio);
        if (pontoApoio.getIdPonto() <= 0) {
            pontoApoio.setIdPonto(pontoApoioDAO.proximoId());
        }
        pontoApoioDAO.inserir(pontoApoio);
        return pontoApoio;
    }

    // Atualizar
    public PontoApoio atualizarBo(int id, PontoApoio pontoApoio) throws SQLException, ExcecoesConexao {
        Validacao.corpo(pontoApoio);
        pontoApoio.setIdPonto(id);
        // Regra de negócio
        validar(pontoApoio);
        if (!pontoApoioDAO.atualizar(pontoApoio)) {
            throw new RecursoNaoEncontradoException("PontoApoio " + id + " não encontrado");
        }
        return pontoApoio;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!pontoApoioDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("PontoApoio " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(PontoApoio pontoApoio) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(pontoApoio.getNome(), "nome");
        Validacao.dominio(pontoApoio.getTipo(), "tipo", "ABRIGO", "ESPERA_COBERTA");
        if (pontoApoio.getCapacidade() != null && pontoApoio.getCapacidade() <= 0)
            throw new RegraNegocioException("capacidade deve ser maior que zero");
        if (Validacao.vazio(pontoApoio.getStAberto())) pontoApoio.setStAberto("N");
        if (Validacao.vazio(pontoApoio.getTemBanheiro())) pontoApoio.setTemBanheiro("N");
        Validacao.dominio(pontoApoio.getStAberto(), "stAberto", "S", "N");
        Validacao.dominio(pontoApoio.getTemBanheiro(), "temBanheiro", "S", "N");
        pontoApoio.setDtAtualizacao(Relogio.agora());
    }

    // Lista com filtros opcionais (?idBairro=1&aberto=S)
    public ArrayList<PontoApoio> selecionarFiltradoBo(Integer idBairro, String aberto) throws SQLException, ExcecoesConexao {
        if (aberto != null) Validacao.dominio(aberto, "aberto", "S", "N");
        return (ArrayList<PontoApoio>) pontoApoioDAO.selecionarFiltrado(idBairro, aberto);
    }

    // Abre ou fecha o ponto (US13): só agente da Defesa Civil
    public PontoApoio atualizarStatusBo(int idPonto, String aberto, int idAgente) throws SQLException, ExcecoesConexao {
        Validacao.dominio(aberto, "aberto", "S", "N");
        new UsuarioBo().exigirAgente(idAgente);
        if (!pontoApoioDAO.atualizarStatus(idPonto, aberto, Relogio.agora())) {
            throw new RecursoNaoEncontradoException("PontoApoio " + idPonto + " não encontrado");
        }
        return buscarPorIdBo(idPonto);
    }
}
