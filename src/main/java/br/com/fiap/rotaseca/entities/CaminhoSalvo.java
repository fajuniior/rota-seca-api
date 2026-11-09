package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Caminho frequente salvo pelo cidadão.
 * Representa a tabela CAMINHO_SALVO do banco Oracle.
 */
public class CaminhoSalvo {

    private int idCaminho;
    private int idUsuario;
    private String nome;
    private int idNoOrigem;
    private int idNoDestino;
    private LocalDateTime dtCriacao;

    public CaminhoSalvo() {
        super();
    }

    public CaminhoSalvo(int idCaminho, int idUsuario, String nome, int idNoOrigem, int idNoDestino, LocalDateTime dtCriacao) {
        super();
        this.idCaminho = idCaminho;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.idNoOrigem = idNoOrigem;
        this.idNoDestino = idNoDestino;
        this.dtCriacao = dtCriacao;
    }

    public int getIdCaminho() {
        return idCaminho;
    }

    public void setIdCaminho(int idCaminho) {
        this.idCaminho = idCaminho;
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

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDateTime dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    @Override
    public String toString() {
        return "CaminhoSalvo{" +
                "idCaminho=" + idCaminho +
                ", idUsuario=" + idUsuario +
                ", nome=" + nome +
                ", idNoOrigem=" + idNoOrigem +
                ", idNoDestino=" + idNoDestino +
                ", dtCriacao=" + dtCriacao +
                '}';
    }
}
