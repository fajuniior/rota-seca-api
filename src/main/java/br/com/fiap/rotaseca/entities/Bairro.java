package br.com.fiap.rotaseca.entities;

/**
 * Bairro atendido pela Rota Seca.
 * Representa a tabela BAIRRO do banco Oracle.
 */
public class Bairro {

    private int idBairro;
    private String nome;
    private String cidade;

    public Bairro() {
        super();
    }

    public Bairro(int idBairro, String nome, String cidade) {
        super();
        this.idBairro = idBairro;
        this.nome = nome;
        this.cidade = cidade;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "Bairro{" +
                "idBairro=" + idBairro +
                ", nome=" + nome +
                ", cidade=" + cidade +
                '}';
    }
}
