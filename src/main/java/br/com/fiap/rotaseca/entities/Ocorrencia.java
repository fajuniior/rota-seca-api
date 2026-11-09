package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Alagamento ou bloqueio reportado por um morador.
 * Representa a tabela OCORRENCIA do banco Oracle.
 */
public class Ocorrencia {

    private int idOcorrencia;
    private int idTrecho;
    private int idUsuario;
    private String tipo;
    private LocalDateTime dtRegistro;
    private String urlFoto;
    private String stValidacao;
    private Integer idAgente;
    private LocalDateTime dtValidacao;
    private LocalDateTime dtExpiracao;

    public Ocorrencia() {
        super();
    }

    public Ocorrencia(int idOcorrencia, int idTrecho, int idUsuario, String tipo, LocalDateTime dtRegistro, String urlFoto, String stValidacao, Integer idAgente, LocalDateTime dtValidacao, LocalDateTime dtExpiracao) {
        super();
        this.idOcorrencia = idOcorrencia;
        this.idTrecho = idTrecho;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.dtRegistro = dtRegistro;
        this.urlFoto = urlFoto;
        this.stValidacao = stValidacao;
        this.idAgente = idAgente;
        this.dtValidacao = dtValidacao;
        this.dtExpiracao = dtExpiracao;
    }

    public int getIdOcorrencia() {
        return idOcorrencia;
    }

    public void setIdOcorrencia(int idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public int getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(int idTrecho) {
        this.idTrecho = idTrecho;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDateTime dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getStValidacao() {
        return stValidacao;
    }

    public void setStValidacao(String stValidacao) {
        this.stValidacao = stValidacao;
    }

    public Integer getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public LocalDateTime getDtValidacao() {
        return dtValidacao;
    }

    public void setDtValidacao(LocalDateTime dtValidacao) {
        this.dtValidacao = dtValidacao;
    }

    public LocalDateTime getDtExpiracao() {
        return dtExpiracao;
    }

    public void setDtExpiracao(LocalDateTime dtExpiracao) {
        this.dtExpiracao = dtExpiracao;
    }

    @Override
    public String toString() {
        return "Ocorrencia{" +
                "idOcorrencia=" + idOcorrencia +
                ", idTrecho=" + idTrecho +
                ", idUsuario=" + idUsuario +
                ", tipo=" + tipo +
                ", dtRegistro=" + dtRegistro +
                ", urlFoto=" + urlFoto +
                ", stValidacao=" + stValidacao +
                ", idAgente=" + idAgente +
                ", dtValidacao=" + dtValidacao +
                ", dtExpiracao=" + dtExpiracao +
                '}';
    }
}
