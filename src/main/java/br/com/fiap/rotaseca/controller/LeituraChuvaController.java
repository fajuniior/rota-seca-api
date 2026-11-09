package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.LeituraChuvaBo;
import br.com.fiap.rotaseca.entities.LeituraChuva;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/leituras-chuva")
public class LeituraChuvaController {

    LeituraChuvaBo leituraChuvaBo = new LeituraChuvaBo();

    // Selecionar
    @GetMapping
    public ArrayList<LeituraChuva> selecionarRs() throws SQLException, ExcecoesConexao {
        return leituraChuvaBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public LeituraChuva buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return leituraChuvaBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<LeituraChuva> inserirRs(@RequestBody LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        LeituraChuva criado = leituraChuvaBo.inserirBo(leituraChuva);
        URI location = URI.create("/leituras-chuva/" + criado.getIdLeitura());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<LeituraChuva> atualizarRs(@PathVariable int id, @RequestBody LeituraChuva leituraChuva) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(leituraChuvaBo.atualizarBo(id, leituraChuva));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        leituraChuvaBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
