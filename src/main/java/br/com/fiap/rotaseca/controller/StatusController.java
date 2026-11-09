package br.com.fiap.rotaseca.controller;

import br.com.fiap.rotaseca.bo.Relogio;
import br.com.fiap.rotaseca.conexoes.ConexaoFactory;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Página inicial e verificação de saúde da API (usada para conferir o deploy no Render).
 */
@RestController
public class StatusController {

    @GetMapping("/")
    public Map<String, Object> inicio() {
        Map<String, Object> info = new LinkedHashMap<String, Object>();
        info.put("api", "Rota Seca");
        info.put("equipe", "NovaCore - 2ESPF");
        info.put("descricao", "Alertas de chuva por bairro, ocorrências de alagamento e pontos de apoio em Guarulhos");
        info.put("aviso", "Protótipo acadêmico com dados simulados. Não substitui as orientações da Defesa Civil (199).");
        info.put("horario", Relogio.agora().toString());
        info.put("recursos", List.of("/bairros", "/usuarios", "/nos-via", "/trechos-via", "/ocorrencias",
                "/confirmacoes", "/leituras-chuva", "/alertas", "/pontos-apoio", "/caminhos",
                "/contatos", "/notificacoes", "/status"));
        return info;
    }

    // Testa a conexão com o Oracle da FIAP
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() throws ExcecoesConexao {
        Map<String, Object> info = new LinkedHashMap<String, Object>();
        try (Connection conexao = new ConexaoFactory().conexao()) {
            info.put("api", "OK");
            info.put("banco", conexao.isValid(5) ? "OK" : "SEM RESPOSTA");
        } catch (SQLException e) {
            throw new ExcecoesConexao(e);
        }
        return ResponseEntity.ok(info);
    }
}
