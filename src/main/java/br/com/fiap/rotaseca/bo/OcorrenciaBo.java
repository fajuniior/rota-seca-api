package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.OcorrenciaDAO;
import br.com.fiap.rotaseca.dto.ValidacaoRequest;
import br.com.fiap.rotaseca.entities.Ocorrencia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Regras de negócio de Ocorrencia.
 */
public class OcorrenciaBo {

    // Uma ocorrência deixa de gerar avisos depois deste tempo (informação velha gera desconfiança)
    public static final int HORAS_VALIDADE = 3;

    OcorrenciaDAO ocorrenciaDAO = new OcorrenciaDAO();

    // Selecionar
    public ArrayList<Ocorrencia> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Ocorrencia>) ocorrenciaDAO.selecionar();
    }

    // Ocorrências ativas (não descartadas e não expiradas)
    public ArrayList<Ocorrencia> selecionarAtivasBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Ocorrencia>) ocorrenciaDAO.selecionarAtivas(Relogio.agora());
    }

    // Buscar por ID
    public Ocorrencia buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        Ocorrencia ocorrencia = ocorrenciaDAO.buscarPorId(id);
        if (ocorrencia == null) {
            throw new RecursoNaoEncontradoException("Ocorrencia " + id + " não encontrada");
        }
        return ocorrencia;
    }

    // Inserir: o morador reporta com um toque; a foto é opcional (US05)
    public Ocorrencia inserirBo(Ocorrencia ocorrencia) throws SQLException, ExcecoesConexao {
        Validacao.corpo(ocorrencia);
        Validacao.dominio(ocorrencia.getTipo(), "tipo", "ALAGAMENTO", "CORREGO_SUBINDO", "VIA_BLOQUEADA");
        // Regra de negócio: datas e status são definidos pelo sistema, não pelo app
        LocalDateTime agora = Relogio.agora();
        ocorrencia.setDtRegistro(agora);
        ocorrencia.setDtExpiracao(agora.plusHours(HORAS_VALIDADE));
        ocorrencia.setStValidacao("PENDENTE");
        ocorrencia.setIdAgente(null);
        ocorrencia.setDtValidacao(null);
        if (Validacao.vazio(ocorrencia.getUrlFoto())) ocorrencia.setUrlFoto(null);
        if (ocorrencia.getIdOcorrencia() <= 0) {
            ocorrencia.setIdOcorrencia(ocorrenciaDAO.proximoId());
        }
        ocorrenciaDAO.inserir(ocorrencia);
        return ocorrencia;
    }

    // Atualizar: o morador pode corrigir o tipo, o trecho ou a foto; a validação tem endpoint próprio
    public Ocorrencia atualizarBo(int id, Ocorrencia dados) throws SQLException, ExcecoesConexao {
        Validacao.corpo(dados);
        Ocorrencia atual = buscarPorIdBo(id);
        Validacao.dominio(dados.getTipo(), "tipo", "ALAGAMENTO", "CORREGO_SUBINDO", "VIA_BLOQUEADA");
        if (!"PENDENTE".equals(atual.getStValidacao())) {
            throw new RegraNegocioException("Ocorrência já validada pela Defesa Civil não pode ser alterada");
        }
        atual.setTipo(dados.getTipo());
        if (dados.getIdTrecho() > 0) atual.setIdTrecho(dados.getIdTrecho());
        atual.setUrlFoto(Validacao.vazio(dados.getUrlFoto()) ? null : dados.getUrlFoto());
        ocorrenciaDAO.atualizar(atual);
        return atual;
    }

    // Validar: agente da Defesa Civil confirma ou descarta (US11)
    public Ocorrencia validarBo(int id, ValidacaoRequest pedido) throws SQLException, ExcecoesConexao {
        Validacao.corpo(pedido);
        Validacao.dominio(pedido.getStValidacao(), "stValidacao", "CONFIRMADA", "DESCARTADA");
        new UsuarioBo().exigirAgente(pedido.getIdAgente());
        buscarPorIdBo(id);
        ocorrenciaDAO.validar(id, pedido.getStValidacao(), pedido.getIdAgente(), Relogio.agora());
        return buscarPorIdBo(id);
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!ocorrenciaDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("Ocorrencia " + id + " não encontrada");
        }
    }
}
