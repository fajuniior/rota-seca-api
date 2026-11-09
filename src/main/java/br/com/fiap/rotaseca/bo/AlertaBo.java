package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.AlertaDAO;
import br.com.fiap.rotaseca.entities.Alerta;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de Alerta.
 */
public class AlertaBo {

    AlertaDAO alertaDAO = new AlertaDAO();

    // Selecionar
    public ArrayList<Alerta> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Alerta>) alertaDAO.selecionar();
    }

    // Buscar por ID
    public Alerta buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        Alerta alerta = alertaDAO.buscarPorId(id);
        if (alerta == null) {
            throw new RecursoNaoEncontradoException("Alerta " + id + " não encontrado");
        }
        return alerta;
    }

    // Inserir
    public Alerta inserirBo(Alerta alerta) throws SQLException, ExcecoesConexao {
        Validacao.corpo(alerta);
        // Regra de negócio
        validar(alerta);
        if (alerta.getIdAlerta() <= 0) {
            alerta.setIdAlerta(alertaDAO.proximoId());
        }
        alertaDAO.inserir(alerta);
        return alerta;
    }

    // Atualizar
    public Alerta atualizarBo(int id, Alerta alerta) throws SQLException, ExcecoesConexao {
        Validacao.corpo(alerta);
        alerta.setIdAlerta(id);
        // Regra de negócio
        validar(alerta);
        if (!alertaDAO.atualizar(alerta)) {
            throw new RecursoNaoEncontradoException("Alerta " + id + " não encontrado");
        }
        return alerta;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!alertaDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("Alerta " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(Alerta alerta) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(alerta.getMensagem(), "mensagem");
        if (alerta.getMensagem().length() > 120)
            throw new RegraNegocioException("A mensagem do alerta deve ter no máximo 120 caracteres");
        Validacao.dominio(alerta.getOrigem(), "origem", "MODELO", "AGENTE");
        Double prob = alerta.getProbChuvaIntensa();
        if (prob != null && (prob < 0 || prob > 1))
            throw new RegraNegocioException("probChuvaIntensa deve estar entre 0 e 1");
        // Regra: alerta do MODELO sem nível informado recebe o nível pela probabilidade de chuva intensa
        if (Validacao.vazio(alerta.getNivel())) {
            if (prob == null)
                throw new RegraNegocioException("Informe o nivel ou a probChuvaIntensa");
            alerta.setNivel(nivelPorProbabilidade(prob));
        }
        Validacao.dominio(alerta.getNivel(), "nivel", "INFORMATIVO", "ATENCAO", "GRAVE");
        if ("AGENTE".equals(alerta.getOrigem())) {
            if (alerta.getIdAgente() == null)
                throw new RegraNegocioException("Alerta de origem AGENTE precisa do idAgente");
            new UsuarioBo().exigirAgente(alerta.getIdAgente());
        }
        if (alerta.getDtInicio() == null) alerta.setDtInicio(Relogio.agora());
        if (alerta.getDtFim() != null && !alerta.getDtFim().isAfter(alerta.getDtInicio()))
            throw new RegraNegocioException("dtFim deve ser posterior a dtInicio");
    }

    // Regra: o nível do alerta sai da probabilidade estimada pelo modelo de Data Science
    public static String nivelPorProbabilidade(double prob) {
        if (prob >= 0.75) return "GRAVE";
        if (prob >= 0.40) return "ATENCAO";
        return "INFORMATIVO";
    }

    // Alerta vigente do bairro
    public Alerta buscarVigenteBo(int idBairro) throws SQLException, ExcecoesConexao {
        new BairroBo().buscarPorIdBo(idBairro);
        Alerta alerta = alertaDAO.buscarVigentePorBairro(idBairro, Relogio.agora());
        if (alerta == null) {
            throw new RecursoNaoEncontradoException("Nenhum alerta vigente para o bairro " + idBairro);
        }
        return alerta;
    }

    // Histórico do bairro
    public ArrayList<Alerta> selecionarPorBairroBo(int idBairro) throws SQLException, ExcecoesConexao {
        new BairroBo().buscarPorIdBo(idBairro);
        return (ArrayList<Alerta>) alertaDAO.selecionarPorBairro(idBairro);
    }
}
