package br.com.fiap.rotaseca.entities;

/**
 * Familiar que recebe os alertas graves.
 * Representa a tabela CONTATO_CONFIANCA do banco Oracle.
 */
public class ContatoConfianca {

    private int idContato;
    private int idUsuario;
    private String nome;
    private String telefone;
    private String parentesco;
    private String stRecebeGrave;

    public ContatoConfianca() {
        super();
    }

    public ContatoConfianca(int idContato, int idUsuario, String nome, String telefone, String parentesco, String stRecebeGrave) {
        super();
        this.idContato = idContato;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.telefone = telefone;
        this.parentesco = parentesco;
        this.stRecebeGrave = stRecebeGrave;
    }

    public int getIdContato() {
        return idContato;
    }

    public void setIdContato(int idContato) {
        this.idContato = idContato;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getStRecebeGrave() {
        return stRecebeGrave;
    }

    public void setStRecebeGrave(String stRecebeGrave) {
        this.stRecebeGrave = stRecebeGrave;
    }

    @Override
    public String toString() {
        return "ContatoConfianca{" +
                "idContato=" + idContato +
                ", idUsuario=" + idUsuario +
                ", nome=" + nome +
                ", telefone=" + telefone +
                ", parentesco=" + parentesco +
                ", stRecebeGrave=" + stRecebeGrave +
                '}';
    }
}
