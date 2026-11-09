package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.AlertaBo;
import br.com.fiap.rotaseca.entities.Alerta;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    AlertaBo alertaBo = new AlertaBo();

    // Selecionar
    @GetMapping
    public ArrayList<Alerta> selecionarRs() throws SQLException, ExcecoesConexao {
        return alertaBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Alerta buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return alertaBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Alerta> inserirRs(@RequestBody Alerta alerta) throws SQLException, ExcecoesConexao {
        Alerta criado = alertaBo.inserirBo(alerta);
        URI location = URI.create("/alertas/" + criado.getIdAlerta());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Alerta> atualizarRs(@PathVariable int id, @RequestBody Alerta alerta) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(alertaBo.atualizarBo(id, alerta));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        alertaBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
