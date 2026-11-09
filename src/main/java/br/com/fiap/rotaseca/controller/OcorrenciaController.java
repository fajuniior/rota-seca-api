package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.Autorizacao;
import br.com.fiap.rotaseca.bo.ConfirmacaoBo;
import br.com.fiap.rotaseca.bo.OcorrenciaBo;
import br.com.fiap.rotaseca.dto.ValidacaoRequest;
import br.com.fiap.rotaseca.entities.Confirmacao;
import br.com.fiap.rotaseca.entities.Ocorrencia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    OcorrenciaBo ocorrenciaBo = new OcorrenciaBo();
    ConfirmacaoBo confirmacaoBo = new ConfirmacaoBo();

    // Selecionar (?ativas=true traz só as não descartadas e não expiradas)
    @GetMapping
    public ArrayList<Ocorrencia> selecionarRs(@RequestParam(defaultValue = "false") boolean ativas)
            throws SQLException, ExcecoesConexao {
        return ativas ? ocorrenciaBo.selecionarAtivasBo() : ocorrenciaBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Ocorrencia buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return ocorrenciaBo.buscarPorIdBo(id);
    }

    // Inserir (reportar ocorrência)
    @PostMapping
    public ResponseEntity<Ocorrencia> inserirRs(@RequestBody Ocorrencia ocorrencia) throws SQLException, ExcecoesConexao {
        Ocorrencia criada = ocorrenciaBo.inserirBo(ocorrencia);
        return ResponseEntity.created(URI.create("/ocorrencias/" + criada.getIdOcorrencia())).body(criada);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Ocorrencia> atualizarRs(@PathVariable int id, @RequestBody Ocorrencia ocorrencia)
            throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(ocorrenciaBo.atualizarBo(id, ocorrencia));
    }

    // Validar (agente da Defesa Civil)
    @PutMapping("/{id}/validacao")
    public ResponseEntity<Ocorrencia> validarRs(@PathVariable int id, @RequestBody ValidacaoRequest pedido,
                                                @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        Autorizacao.exigirAgente(token);
        return ResponseEntity.ok(ocorrenciaBo.validarBo(id, pedido));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        ocorrenciaBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }

    // Confirmações da ocorrência
    @GetMapping("/{id}/confirmacoes")
    public ArrayList<Confirmacao> confirmacoesRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return confirmacaoBo.selecionarPorOcorrenciaBo(id);
    }

    // Confirmar ocorrência (outro morador viu a mesma coisa)
    @PostMapping("/{id}/confirmacoes")
    public ResponseEntity<Confirmacao> confirmarRs(@PathVariable int id, @RequestBody Confirmacao confirmacao)
            throws SQLException, ExcecoesConexao {
        confirmacao.setIdOcorrencia(id);
        Confirmacao criada = confirmacaoBo.inserirBo(confirmacao);
        return ResponseEntity.created(URI.create("/confirmacoes/" + criada.getIdConfirmacao())).body(criada);
    }
}
