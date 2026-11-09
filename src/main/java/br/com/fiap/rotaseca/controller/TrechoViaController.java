package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.TrechoViaBo;
import br.com.fiap.rotaseca.entities.TrechoVia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/trechos-via")
public class TrechoViaController {

    TrechoViaBo trechoViaBo = new TrechoViaBo();

    // Selecionar
    @GetMapping
    public ArrayList<TrechoVia> selecionarRs() throws SQLException, ExcecoesConexao {
        return trechoViaBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public TrechoVia buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return trechoViaBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<TrechoVia> inserirRs(@RequestBody TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        TrechoVia criado = trechoViaBo.inserirBo(trechoVia);
        URI location = URI.create("/trechos-via/" + criado.getIdTrecho());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<TrechoVia> atualizarRs(@PathVariable int id, @RequestBody TrechoVia trechoVia) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(trechoViaBo.atualizarBo(id, trechoVia));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        trechoViaBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
