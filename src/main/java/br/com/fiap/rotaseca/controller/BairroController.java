package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.BairroBo;
import br.com.fiap.rotaseca.entities.Bairro;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/bairros")
public class BairroController {

    BairroBo bairroBo = new BairroBo();

    // Selecionar
    @GetMapping
    public ArrayList<Bairro> selecionarRs() throws SQLException, ExcecoesConexao {
        return bairroBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Bairro buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return bairroBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Bairro> inserirRs(@RequestBody Bairro bairro) throws SQLException, ExcecoesConexao {
        Bairro criado = bairroBo.inserirBo(bairro);
        URI location = URI.create("/bairros/" + criado.getIdBairro());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Bairro> atualizarRs(@PathVariable int id, @RequestBody Bairro bairro) throws SQLException, ExcecoesConexao {
        return ResponseEntity.ok(bairroBo.atualizarBo(id, bairro));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        bairroBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }

    // Alerta vigente do bairro (US01)
    @GetMapping("/{id}/alerta-atual")
    public br.com.fiap.rotaseca.entities.Alerta alertaAtualRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return new br.com.fiap.rotaseca.bo.AlertaBo().buscarVigenteBo(id);
    }

    // Histórico de alertas do bairro
    @GetMapping("/{id}/alertas")
    public ArrayList<br.com.fiap.rotaseca.entities.Alerta> alertasRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return new br.com.fiap.rotaseca.bo.AlertaBo().selecionarPorBairroBo(id);
    }

    // Leituras de chuva do bairro
    @GetMapping("/{id}/leituras-chuva")
    public ArrayList<br.com.fiap.rotaseca.entities.LeituraChuva> leiturasRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return new br.com.fiap.rotaseca.bo.LeituraChuvaBo().selecionarPorBairroBo(id);
    }
}
