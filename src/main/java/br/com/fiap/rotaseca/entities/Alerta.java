package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Alerta de chuva de um bairro.
 * Representa a tabela ALERTA do banco Oracle.
 */
public class Alerta {

    private int idAlerta;
    private int idBairro;
    private String nivel;
    private String mensagem;
    private Double probChuvaIntensa;
    private String origem;
    private Integer idAgente;
    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;

    public Alerta() {
        super();
    }

    public Alerta(int idAlerta, int idBairro, String nivel, String mensagem, Double probChuvaIntensa, String origem, Integer idAgente, LocalDateTime dtInicio, LocalDateTime dtFim) {
        super();
        this.idAlerta = idAlerta;
        this.idBairro = idBairro;
        this.nivel = nivel;
        this.mensagem = mensagem;
        this.probChuvaIntensa = probChuvaIntensa;
        this.origem = origem;
        this.idAgente = idAgente;
        this.dtInicio = dtInicio;
        this.dtFim = dtFim;
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Double getProbChuvaIntensa() {
        return probChuvaIntensa;
    }

    public void setProbChuvaIntensa(Double probChuvaIntensa) {
        this.probChuvaIntensa = probChuvaIntensa;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public Integer getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public LocalDateTime getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(LocalDateTime dtInicio) {
        this.dtInicio = dtInicio;
    }

    public LocalDateTime getDtFim() {
        return dtFim;
    }

    public void setDtFim(LocalDateTime dtFim) {
        this.dtFim = dtFim;
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "idAlerta=" + idAlerta +
                ", idBairro=" + idBairro +
                ", nivel=" + nivel +
                ", mensagem=" + mensagem +
                ", probChuvaIntensa=" + probChuvaIntensa +
                ", origem=" + origem +
                ", idAgente=" + idAgente +
                ", dtInicio=" + dtInicio +
                ", dtFim=" + dtFim +
                '}';
    }
}
