package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.ConfirmacaoBo;
import br.com.fiap.rotaseca.entities.Confirmacao;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/confirmacoes")
public class ConfirmacaoController {

    ConfirmacaoBo confirmacaoBo = new ConfirmacaoBo();

    // Selecionar
    @GetMapping
    public ArrayList<Confirmacao> selecionarRs() throws SQLException, ExcecoesConexao {
        return confirmacaoBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Confirmacao buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return confirmacaoBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Confirmacao> inserirRs(@RequestBody Confirmacao confirmacao) throws SQLException, ExcecoesConexao {
        Confirmacao criado = confirmacaoBo.inserirBo(confirmacao);
        URI location = URI.create("/confirmacoes/" + criado.getIdConfirmacao());
        return ResponseEntity.created(location).body(criado);
    }

    // Não há PUT: a confirmação é imutável. Para corrigir, exclua e confirme de novo (passando pelas regras).

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        confirmacaoBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
