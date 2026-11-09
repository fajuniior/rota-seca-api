package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Leitura de precipitação de um bairro.
 * Representa a tabela LEITURA_CHUVA do banco Oracle.
 */
public class LeituraChuva {

    private int idLeitura;
    private int idBairro;
    private LocalDateTime dtLeitura;
    private double precipitacaoMm;
    private String fonte;

    public LeituraChuva() {
        super();
    }

    public LeituraChuva(int idLeitura, int idBairro, LocalDateTime dtLeitura, double precipitacaoMm, String fonte) {
        super();
        this.idLeitura = idLeitura;
        this.idBairro = idBairro;
        this.dtLeitura = dtLeitura;
        this.precipitacaoMm = precipitacaoMm;
        this.fonte = fonte;
    }

    public int getIdLeitura() {
        return idLeitura;
    }

    public void setIdLeitura(int idLeitura) {
        this.idLeitura = idLeitura;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public LocalDateTime getDtLeitura() {
        return dtLeitura;
    }

    public void setDtLeitura(LocalDateTime dtLeitura) {
        this.dtLeitura = dtLeitura;
    }

    public double getPrecipitacaoMm() {
        return precipitacaoMm;
    }

    public void setPrecipitacaoMm(double precipitacaoMm) {
        this.precipitacaoMm = precipitacaoMm;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    @Override
    public String toString() {
        return "LeituraChuva{" +
                "idLeitura=" + idLeitura +
                ", idBairro=" + idBairro +
                ", dtLeitura=" + dtLeitura +
                ", precipitacaoMm=" + precipitacaoMm +
                ", fonte=" + fonte +
                '}';
    }
}
