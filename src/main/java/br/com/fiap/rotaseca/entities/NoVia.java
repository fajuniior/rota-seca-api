package br.com.fiap.rotaseca.entities;

/**
 * Nó do grafo de ruas (cruzamento).
 * Representa a tabela NO_VIA do banco Oracle.
 */
public class NoVia {

    private int idNo;
    private int idBairro;
    private String referencia;
    private double latitude;
    private double longitude;

    public NoVia() {
        super();
    }

    public NoVia(int idNo, int idBairro, String referencia, double latitude, double longitude) {
        super();
        this.idNo = idNo;
        this.idBairro = idBairro;
        this.referencia = referencia;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIdNo() {
        return idNo;
    }

    public void setIdNo(int idNo) {
        this.idNo = idNo;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "NoVia{" +
                "idNo=" + idNo +
                ", idBairro=" + idBairro +
                ", referencia=" + referencia +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
