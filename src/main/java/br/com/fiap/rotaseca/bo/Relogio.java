package br.com.fiap.rotaseca.bo;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Hora oficial da aplicação: horário de Brasília,
 * mesmo quando o servidor (Render) está em UTC.
 */
public final class Relogio {

    public static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");

    private Relogio() {
    }

    public static LocalDateTime agora() {
        return LocalDateTime.now(FUSO).withNano(0);
    }
}
