package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.ContatoConfiancaBo;
import br.com.fiap.rotaseca.entities.ContatoConfianca;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/contatos")
public class ContatoConfiancaController {

    ContatoConfiancaBo contatoConfiancaBo = new ContatoConfiancaBo();

    // Selecionar
    @GetMapping
    public ArrayList<ContatoConfianca> selecionarRs() throws SQLException, ExcecoesConexao {
        return contatoConfiancaBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ContatoConfianca buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return contatoConfiancaBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<ContatoConfianca> inserirRs(@RequestBody ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        ContatoConfianca criado = contatoConfiancaBo.inserirBo(contatoConfianca);
        URI location = URI.create("/contatos/" + criado.getIdContato());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<ContatoConfianca> atualizarRs(@PathVariable int id, @RequestBody ContatoConfianca contatoConfianca) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(contatoConfiancaBo.atualizarBo(id, contatoConfianca));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        contatoConfiancaBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
