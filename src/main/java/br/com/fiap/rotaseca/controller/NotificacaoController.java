package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.NotificacaoBo;
import br.com.fiap.rotaseca.entities.Notificacao;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    NotificacaoBo notificacaoBo = new NotificacaoBo();

    // Selecionar
    @GetMapping
    public ArrayList<Notificacao> selecionarRs() throws SQLException, ExcecoesConexao {
        return notificacaoBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Notificacao buscarPorIdRs(@PathVariable long id) throws SQLException, ExcecoesConexao {
        return notificacaoBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Notificacao> inserirRs(@RequestBody Notificacao notificacao) throws SQLException, ExcecoesConexao {
        Notificacao criado = notificacaoBo.inserirBo(notificacao);
        URI location = URI.create("/notificacoes/" + criado.getIdNotificacao());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Notificacao> atualizarRs(@PathVariable long id, @RequestBody Notificacao notificacao) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(notificacaoBo.atualizarBo(id, notificacao));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable long id) throws SQLException, ExcecoesConexao {
        notificacaoBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }
}
