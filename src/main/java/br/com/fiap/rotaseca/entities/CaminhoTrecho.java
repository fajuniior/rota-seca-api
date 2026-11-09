package br.com.fiap.rotaseca.entities;

/**
 * Trecho que compõe um caminho salvo, em ordem.
 * Representa a tabela CAMINHO_TRECHO do banco Oracle.
 */
public class CaminhoTrecho {

    private int idCaminho;
    private int nrOrdem;
    private int idTrecho;

    public CaminhoTrecho() {
        super();
    }

    public CaminhoTrecho(int idCaminho, int nrOrdem, int idTrecho) {
        super();
        this.idCaminho = idCaminho;
        this.nrOrdem = nrOrdem;
        this.idTrecho = idTrecho;
    }

    public int getIdCaminho() {
        return idCaminho;
    }

    public void setIdCaminho(int idCaminho) {
        this.idCaminho = idCaminho;
    }

    public int getNrOrdem() {
        return nrOrdem;
    }

    public void setNrOrdem(int nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public int getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(int idTrecho) {
        this.idTrecho = idTrecho;
    }

    @Override
    public String toString() {
        return "CaminhoTrecho{" +
                "idCaminho=" + idCaminho +
                ", nrOrdem=" + nrOrdem +
                ", idTrecho=" + idTrecho +
                '}';
    }
}
