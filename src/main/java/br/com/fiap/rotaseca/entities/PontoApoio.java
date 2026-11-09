package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Abrigo ou local de espera coberta.
 * Representa a tabela PONTO_APOIO do banco Oracle.
 */
public class PontoApoio {

    private int idPonto;
    private int idBairro;
    private Integer idNo;
    private String nome;
    private String tipo;
    private String endereco;
    private Integer capacidade;
    private String horario;
    private String stAberto;
    private String temBanheiro;
    private LocalDateTime dtAtualizacao;

    public PontoApoio() {
        super();
    }

    public PontoApoio(int idPonto, int idBairro, Integer idNo, String nome, String tipo, String endereco, Integer capacidade, String horario, String stAberto, String temBanheiro, LocalDateTime dtAtualizacao) {
        super();
        this.idPonto = idPonto;
        this.idBairro = idBairro;
        this.idNo = idNo;
        this.nome = nome;
        this.tipo = tipo;
        this.endereco = endereco;
        this.capacidade = capacidade;
        this.horario = horario;
        this.stAberto = stAberto;
        this.temBanheiro = temBanheiro;
        this.dtAtualizacao = dtAtualizacao;
    }

    public int getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(int idPonto) {
        this.idPonto = idPonto;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public Integer getIdNo() {
        return idNo;
    }

    public void setIdNo(Integer idNo) {
        this.idNo = idNo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getStAberto() {
        return stAberto;
    }

    public void setStAberto(String stAberto) {
        this.stAberto = stAberto;
    }

    public String getTemBanheiro() {
        return temBanheiro;
    }

    public void setTemBanheiro(String temBanheiro) {
        this.temBanheiro = temBanheiro;
    }

    public LocalDateTime getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(LocalDateTime dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }

    @Override
    public String toString() {
        return "PontoApoio{" +
                "idPonto=" + idPonto +
                ", idBairro=" + idBairro +
                ", idNo=" + idNo +
                ", nome=" + nome +
                ", tipo=" + tipo +
                ", endereco=" + endereco +
                ", capacidade=" + capacidade +
                ", horario=" + horario +
                ", stAberto=" + stAberto +
                ", temBanheiro=" + temBanheiro +
                ", dtAtualizacao=" + dtAtualizacao +
                '}';
    }
}
