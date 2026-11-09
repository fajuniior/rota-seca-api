package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.CaminhoTrechoBo;
import br.com.fiap.rotaseca.entities.CaminhoTrecho;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/caminhos/{idCaminho}/trechos")
public class CaminhoTrechoController {

    CaminhoTrechoBo caminhoTrechoBo = new CaminhoTrechoBo();

    // Selecionar trechos do caminho
    @GetMapping
    public ArrayList<CaminhoTrecho> selecionarRs(@PathVariable int idCaminho) throws SQLException, ExcecoesConexao {
        return caminhoTrechoBo.selecionarPorCaminhoBo(idCaminho);
    }

    // Inserir trecho no caminho
    @PostMapping
    public ResponseEntity<CaminhoTrecho> inserirRs(@PathVariable int idCaminho, @RequestBody CaminhoTrecho caminhoTrecho)
            throws SQLException, ExcecoesConexao {
        CaminhoTrecho criado = caminhoTrechoBo.inserirBo(idCaminho, caminhoTrecho);
        return ResponseEntity.created(URI.create("/caminhos/" + idCaminho + "/trechos/" + criado.getNrOrdem())).body(criado);
    }

    // Atualizar o trecho de uma posição
    @PutMapping("/{nrOrdem}")
    public ResponseEntity<CaminhoTrecho> atualizarRs(@PathVariable int idCaminho, @PathVariable int nrOrdem,
                                                     @RequestBody CaminhoTrecho caminhoTrecho) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(caminhoTrechoBo.atualizarBo(idCaminho, nrOrdem, caminhoTrecho));
    }

    // Deletar uma posição
    @DeleteMapping("/{nrOrdem}")
    public ResponseEntity<Void> deletarRs(@PathVariable int idCaminho, @PathVariable int nrOrdem) throws SQLException, ExcecoesConexao {
        caminhoTrechoBo.deletarBo(idCaminho, nrOrdem);
        return ResponseEntity.noContent().build();
    }
}
