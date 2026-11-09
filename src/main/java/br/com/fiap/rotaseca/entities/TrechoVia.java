package br.com.fiap.rotaseca.entities;

/**
 * Trecho de rua entre dois nós (aresta do grafo).
 * Representa a tabela TRECHO_VIA do banco Oracle.
 */
public class TrechoVia {

    private int idTrecho;
    private int idNoOrigem;
    private int idNoDestino;
    private String nomeVia;
    private int distanciaM;
    private int nivelRisco;
    private String stTrecho;

    public TrechoVia() {
        super();
    }

    public TrechoVia(int idTrecho, int idNoOrigem, int idNoDestino, String nomeVia, int distanciaM, int nivelRisco, String stTrecho) {
        super();
        this.idTrecho = idTrecho;
        this.idNoOrigem = idNoOrigem;
        this.idNoDestino = idNoDestino;
        this.nomeVia = nomeVia;
        this.distanciaM = distanciaM;
        this.nivelRisco = nivelRisco;
        this.stTrecho = stTrecho;
    }

    public int getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(int idTrecho) {
        this.idTrecho = idTrecho;
    }

    public int getIdNoOrigem() {
        return idNoOrigem;
    }

    public void setIdNoOrigem(int idNoOrigem) {
        this.idNoOrigem = idNoOrigem;
    }

    public int getIdNoDestino() {
        return idNoDestino;
    }

    public void setIdNoDestino(int idNoDestino) {
        this.idNoDestino = idNoDestino;
    }

    public String getNomeVia() {
        return nomeVia;
    }

    public void setNomeVia(String nomeVia) {
        this.nomeVia = nomeVia;
    }

    public int getDistanciaM() {
        return distanciaM;
    }

    public void setDistanciaM(int distanciaM) {
        this.distanciaM = distanciaM;
    }

    public int getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(int nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public String getStTrecho() {
        return stTrecho;
    }

    public void setStTrecho(String stTrecho) {
        this.stTrecho = stTrecho;
    }

    @Override
    public String toString() {
        return "TrechoVia{" +
                "idTrecho=" + idTrecho +
                ", idNoOrigem=" + idNoOrigem +
                ", idNoDestino=" + idNoDestino +
                ", nomeVia=" + nomeVia +
                ", distanciaM=" + distanciaM +
                ", nivelRisco=" + nivelRisco +
                ", stTrecho=" + stTrecho +
                '}';
    }
}
