package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.NoViaBo;
import br.com.fiap.rotaseca.entities.NoVia;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/nos-via")
public class NoViaController {

    NoViaBo noViaBo = new NoViaBo();

    // Selecionar
    @GetMapping
    public ArrayList<NoVia> selecionarRs() throws SQLException, ExcecoesConexao {
        return noViaBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public NoVia buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return noViaBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<NoVia> inserirRs(@RequestBody NoVia noVia) throws SQLException, ExcecoesConexao {
        NoVia criado = noViaBo.inserirBo(noVia);
        URI location = URI.create("/nos-via/" + criado.getIdNo());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<NoVia> atualizarRs(@PathVariable int id, @RequestBody NoVia noVia) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(noViaBo.atualizarBo(id, noVia));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        noViaBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
