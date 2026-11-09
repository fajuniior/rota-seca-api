package br.com.fiap.rotaseca.dto;

/**
 * Corpo da requisição PUT /ocorrencias/{id}/validacao.
 * stValidacao: CONFIRMADA ou DESCARTADA.
 */
public class ValidacaoRequest {

    private int idAgente;
    private String stValidacao;

    public int getIdAgente() { return idAgente; }
    public void setIdAgente(int idAgente) { this.idAgente = idAgente; }
    public String getStValidacao() { return stValidacao; }
    public void setStValidacao(String stValidacao) { this.stValidacao = stValidacao; }
}
