package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Registro de um aviso enviado.
 * Representa a tabela NOTIFICACAO do banco Oracle.
 */
public class Notificacao {

    private long idNotificacao;
    private Integer idAlerta;
    private Integer idOcorrencia;
    private Integer idUsuario;
    private Integer idContato;
    private String canal;
    private LocalDateTime dtEnvio;
    private String stEntrega;

    public Notificacao() {
        super();
    }

    public Notificacao(long idNotificacao, Integer idAlerta, Integer idOcorrencia, Integer idUsuario, Integer idContato, String canal, LocalDateTime dtEnvio, String stEntrega) {
        super();
        this.idNotificacao = idNotificacao;
        this.idAlerta = idAlerta;
        this.idOcorrencia = idOcorrencia;
        this.idUsuario = idUsuario;
        this.idContato = idContato;
        this.canal = canal;
        this.dtEnvio = dtEnvio;
        this.stEntrega = stEntrega;
    }

    public long getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(long idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Integer getIdOcorrencia() {
        return idOcorrencia;
    }

    public void setIdOcorrencia(Integer idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdContato() {
        return idContato;
    }

    public void setIdContato(Integer idContato) {
        this.idContato = idContato;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public LocalDateTime getDtEnvio() {
        return dtEnvio;
    }

    public void setDtEnvio(LocalDateTime dtEnvio) {
        this.dtEnvio = dtEnvio;
    }

    public String getStEntrega() {
        return stEntrega;
    }

    public void setStEntrega(String stEntrega) {
        this.stEntrega = stEntrega;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "idNotificacao=" + idNotificacao +
                ", idAlerta=" + idAlerta +
                ", idOcorrencia=" + idOcorrencia +
                ", idUsuario=" + idUsuario +
                ", idContato=" + idContato +
                ", canal=" + canal +
                ", dtEnvio=" + dtEnvio +
                ", stEntrega=" + stEntrega +
                '}';
    }
}
