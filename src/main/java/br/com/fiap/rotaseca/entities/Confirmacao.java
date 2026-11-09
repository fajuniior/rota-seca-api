package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Confirmação de uma ocorrência por outro morador.
 * Representa a tabela CONFIRMACAO do banco Oracle.
 */
public class Confirmacao {

    private int idConfirmacao;
    private int idOcorrencia;
    private int idUsuario;
    private LocalDateTime dtConfirmacao;

    public Confirmacao() {
        super();
    }

    public Confirmacao(int idConfirmacao, int idOcorrencia, int idUsuario, LocalDateTime dtConfirmacao) {
        super();
        this.idConfirmacao = idConfirmacao;
        this.idOcorrencia = idOcorrencia;
        this.idUsuario = idUsuario;
        this.dtConfirmacao = dtConfirmacao;
    }

    public int getIdConfirmacao() {
        return idConfirmacao;
    }

    public void setIdConfirmacao(int idConfirmacao) {
        this.idConfirmacao = idConfirmacao;
    }

    public int getIdOcorrencia() {
        return idOcorrencia;
    }

    public void setIdOcorrencia(int idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDtConfirmacao() {
        return dtConfirmacao;
    }

    public void setDtConfirmacao(LocalDateTime dtConfirmacao) {
        this.dtConfirmacao = dtConfirmacao;
    }

    @Override
    public String toString() {
        return "Confirmacao{" +
                "idConfirmacao=" + idConfirmacao +
                ", idOcorrencia=" + idOcorrencia +
                ", idUsuario=" + idUsuario +
                ", dtConfirmacao=" + dtConfirmacao +
                '}';
    }
}
