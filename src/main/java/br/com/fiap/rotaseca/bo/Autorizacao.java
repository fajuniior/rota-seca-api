package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.excecoes.AcessoNegadoException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Controle de acesso das operações oficiais (validar ocorrência, emitir alerta,
 * abrir/fechar ponto de apoio e cadastrar agentes).
 *
 * Nesta versão acadêmica a integração com a Defesa Civil é SIMULADA: em vez de login,
 * o agente envia a credencial no cabeçalho "X-Token-Agente". O valor fica na variável
 * de ambiente AGENTE_TOKEN (Render/IntelliJ) e nunca no código. Sem a variável
 * configurada, todas essas operações ficam bloqueadas.
 */
public final class Autorizacao {

    public static final String CABECALHO = "X-Token-Agente";

    private Autorizacao() {
    }

    public static void exigirAgente(String tokenInformado) {
        String esperado = System.getenv("AGENTE_TOKEN");
        if (esperado == null || esperado.isBlank()) {
            throw new AcessoNegadoException("Operações de agente desativadas: variável AGENTE_TOKEN não configurada");
        }
        if (tokenInformado == null || !MessageDigest.isEqual(
                esperado.getBytes(StandardCharsets.UTF_8), tokenInformado.getBytes(StandardCharsets.UTF_8))) {
            throw new AcessoNegadoException("Operação exclusiva de agentes da Defesa Civil: informe uma credencial válida no cabeçalho " + CABECALHO);
        }
    }

    // Perfis com poder oficial (AGENTE e ADMIN) só podem ser criados/alterados por quem tem a credencial
    public static boolean perfilPrivilegiado(String tipoPerfil) {
        return "AGENTE".equals(tipoPerfil) || "ADMIN".equals(tipoPerfil);
    }
}
