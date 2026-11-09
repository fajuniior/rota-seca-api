package br.com.fiap.rotaseca.dto;

import java.time.LocalDateTime;

/**
 * Resultado da consulta de avisos: uma ocorrência ativa que afeta
 * um caminho salvo do usuário.
 */
public class AvisoCaminho {

    private int idCaminho;
    private String caminho;
    private int idTrecho;
    private String nomeVia;
    private int idOcorrencia;
    private String tipo;
    private String stValidacao;
    private LocalDateTime dtRegistro;
    private int confirmacoes;

    public int getIdCaminho() { return idCaminho; }
    public void setIdCaminho(int idCaminho) { this.idCaminho = idCaminho; }
    public String getCaminho() { return caminho; }
    public void setCaminho(String caminho) { this.caminho = caminho; }
    public int getIdTrecho() { return idTrecho; }
    public void setIdTrecho(int idTrecho) { this.idTrecho = idTrecho; }
    public String getNomeVia() { return nomeVia; }
    public void setNomeVia(String nomeVia) { this.nomeVia = nomeVia; }
    public int getIdOcorrencia() { return idOcorrencia; }
    public void setIdOcorrencia(int idOcorrencia) { this.idOcorrencia = idOcorrencia; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getStValidacao() { return stValidacao; }
    public void setStValidacao(String stValidacao) { this.stValidacao = stValidacao; }
    public LocalDateTime getDtRegistro() { return dtRegistro; }
    public void setDtRegistro(LocalDateTime dtRegistro) { this.dtRegistro = dtRegistro; }
    public int getConfirmacoes() { return confirmacoes; }
    public void setConfirmacoes(int confirmacoes) { this.confirmacoes = confirmacoes; }

    // Confiável quando tem pelo menos 2 confirmações de vizinhos ou foi confirmada pela Defesa Civil
    public boolean isConfiavel() {
        return "CONFIRMADA".equals(stValidacao) || confirmacoes >= 2;
    }
}
