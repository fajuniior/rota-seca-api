package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.util.Arrays;

/**
 * Validações reutilizadas pelos BOs.
 */
public final class Validacao {

    private Validacao() {
    }

    public static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }

    public static void corpo(Object objeto) {
        if (objeto == null) throw new RegraNegocioException("O corpo da requisição é obrigatório");
    }

    public static void obrigatorio(String valor, String campo) {
        if (vazio(valor)) throw new RegraNegocioException("O campo " + campo + " é obrigatório");
    }

    public static void dominio(String valor, String campo, String... permitidos) {
        if (vazio(valor) || !Arrays.asList(permitidos).contains(valor)) {
            throw new RegraNegocioException("O campo " + campo + " deve ser um destes valores: " + String.join(", ", permitidos));
        }
    }
}
