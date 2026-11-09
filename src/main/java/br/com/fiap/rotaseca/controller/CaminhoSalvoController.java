package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.CaminhoSalvoBo;
import br.com.fiap.rotaseca.entities.CaminhoSalvo;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/caminhos")
public class CaminhoSalvoController {

    CaminhoSalvoBo caminhoSalvoBo = new CaminhoSalvoBo();

    // Selecionar
    @GetMapping
    public ArrayList<CaminhoSalvo> selecionarRs() throws SQLException, ExcecoesConexao {
        return caminhoSalvoBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public CaminhoSalvo buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return caminhoSalvoBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<CaminhoSalvo> inserirRs(@RequestBody CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        CaminhoSalvo criado = caminhoSalvoBo.inserirBo(caminhoSalvo);
        URI location = URI.create("/caminhos/" + criado.getIdCaminho());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<CaminhoSalvo> atualizarRs(@PathVariable int id, @RequestBody CaminhoSalvo caminhoSalvo) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(caminhoSalvoBo.atualizarBo(id, caminhoSalvo));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        caminhoSalvoBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
