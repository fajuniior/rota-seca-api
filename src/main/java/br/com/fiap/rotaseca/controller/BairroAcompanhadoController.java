package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.BairroAcompanhadoBo;
import br.com.fiap.rotaseca.entities.BairroAcompanhado;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/usuarios/{idUsuario}/bairros-acompanhados")
public class BairroAcompanhadoController {

    BairroAcompanhadoBo bairroAcompanhadoBo = new BairroAcompanhadoBo();

    // Selecionar
    @GetMapping
    public ArrayList<BairroAcompanhado> selecionarRs(@PathVariable int idUsuario) throws SQLException, ExcecoesConexao {
        return bairroAcompanhadoBo.selecionarPorUsuarioBo(idUsuario);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<BairroAcompanhado> inserirRs(@PathVariable int idUsuario, @RequestBody BairroAcompanhado acompanhado)
            throws SQLException, ExcecoesConexao {
        BairroAcompanhado criado = bairroAcompanhadoBo.inserirBo(idUsuario, acompanhado);
        return ResponseEntity.created(URI.create("/usuarios/" + idUsuario + "/bairros-acompanhados/" + criado.getIdBairro())).body(criado);
    }

    // Atualizar o motivo
    @PutMapping("/{idBairro}")
    public ResponseEntity<BairroAcompanhado> atualizarRs(@PathVariable int idUsuario, @PathVariable int idBairro,
                                                         @RequestBody BairroAcompanhado acompanhado) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(bairroAcompanhadoBo.atualizarBo(idUsuario, idBairro, acompanhado));
    }

    // Deletar
    @DeleteMapping("/{idBairro}")
    public ResponseEntity<Void> deletarRs(@PathVariable int idUsuario, @PathVariable int idBairro) throws SQLException, ExcecoesConexao {
        bairroAcompanhadoBo.deletarBo(idUsuario, idBairro);
        return ResponseEntity.noContent().build();
    }
}
