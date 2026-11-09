package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.Autorizacao;
import br.com.fiap.rotaseca.bo.PontoApoioBo;
import br.com.fiap.rotaseca.entities.PontoApoio;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/pontos-apoio")
public class PontoApoioController {

    PontoApoioBo pontoApoioBo = new PontoApoioBo();

    // Selecionar (filtros opcionais: ?idBairro=1&aberto=S)
    @GetMapping
    public ArrayList<PontoApoio> selecionarRs(@RequestParam(required = false) Integer idBairro,
                                              @RequestParam(required = false) String aberto)
            throws SQLException, ExcecoesConexao {
        return pontoApoioBo.selecionarFiltradoBo(idBairro, aberto);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public PontoApoio buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return pontoApoioBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<PontoApoio> inserirRs(@RequestBody PontoApoio pontoApoio, @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        Autorizacao.exigirAgente(token);
        PontoApoio criado = pontoApoioBo.inserirBo(pontoApoio);
        return ResponseEntity.created(URI.create("/pontos-apoio/" + criado.getIdPonto())).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<PontoApoio> atualizarRs(@PathVariable int id, @RequestBody PontoApoio pontoApoio,
                                              @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        Autorizacao.exigirAgente(token);
        return ResponseEntity.ok(pontoApoioBo.atualizarBo(id, pontoApoio));
    }

    // Abrir ou fechar o ponto (agente): PUT /pontos-apoio/1/status?aberto=S&idAgente=5
    @PutMapping("/{id}/status")
    public ResponseEntity<PontoApoio> statusRs(@PathVariable int id, @RequestParam String aberto,
                                               @RequestParam int idAgente, @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        Autorizacao.exigirAgente(token);
        return ResponseEntity.ok(pontoApoioBo.atualizarStatusBo(id, aberto, idAgente));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id, @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        Autorizacao.exigirAgente(token);
        pontoApoioBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
