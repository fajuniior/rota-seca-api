package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.Autorizacao;
import br.com.fiap.rotaseca.bo.UsuarioBo;
import br.com.fiap.rotaseca.entities.Usuario;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    UsuarioBo usuarioBo = new UsuarioBo();

    // Selecionar
    @GetMapping
    public ArrayList<Usuario> selecionarRs() throws SQLException, ExcecoesConexao {
        return usuarioBo.selecionarBo();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Usuario buscarPorIdRs(@PathVariable int id) throws SQLException, ExcecoesConexao {
        return usuarioBo.buscarPorIdBo(id);
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Usuario> inserirRs(@RequestBody Usuario usuario, @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        if (usuario != null && Autorizacao.perfilPrivilegiado(usuario.getTipoPerfil())) Autorizacao.exigirAgente(token);
        Usuario criado = usuarioBo.inserirBo(usuario);
        URI location = URI.create("/usuarios/" + criado.getIdUsuario());
        return ResponseEntity.created(location).body(criado);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarRs(@PathVariable int id, @RequestBody Usuario usuario,
                                               @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        // Promover alguém a agente, ou alterar um agente existente, é operação oficial
        if ((usuario != null && Autorizacao.perfilPrivilegiado(usuario.getTipoPerfil()))
                || Autorizacao.perfilPrivilegiado(usuarioBo.buscarPorIdBo(id).getTipoPerfil())) Autorizacao.exigirAgente(token);
        return ResponseEntity.ok(usuarioBo.atualizarBo(id, usuario));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id, @RequestHeader(value = Autorizacao.CABECALHO, required = false) String token)
            throws SQLException, ExcecoesConexao {
        if (Autorizacao.perfilPrivilegiado(usuarioBo.buscarPorIdBo(id).getTipoPerfil())) Autorizacao.exigirAgente(token);
        usuarioBo.deletarBo(id);
        return ResponseEntity.noContent().build();
    }

    // Avisos de ocorrências nos caminhos salvos do usuário (US02)
    @GetMapping("/{id}/avisos")
    public java.util.List<br.com.fiap.rotaseca.dto.AvisoCaminho> avisosRs(@PathVariable int id)
            throws SQLException, ExcecoesConexao {
        return usuarioBo.avisosDosCaminhosBo(id);
    }

    // Caminhos salvos do usuário
    @GetMapping("/{id}/caminhos")
    public ArrayList<br.com.fiap.rotaseca.entities.CaminhoSalvo> caminhosRs(@PathVariable int id)
            throws SQLException, ExcecoesConexao {
        return new br.com.fiap.rotaseca.bo.CaminhoSalvoBo().selecionarPorUsuarioBo(id);
    }

    // Contatos de confiança do usuário
    @GetMapping("/{id}/contatos")
    public ArrayList<br.com.fiap.rotaseca.entities.ContatoConfianca> contatosRs(@PathVariable int id)
            throws SQLException, ExcecoesConexao {
        return new br.com.fiap.rotaseca.bo.ContatoConfiancaBo().selecionarPorUsuarioBo(id);
    }
}
