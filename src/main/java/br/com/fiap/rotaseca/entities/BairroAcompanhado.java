package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Bairro de um familiar acompanhado pelo cidadão.
 * Representa a tabela BAIRRO_ACOMPANHADO do banco Oracle.
 */
public class BairroAcompanhado {

    private int idUsuario;
    private int idBairro;
    private String motivo;
    private LocalDateTime dtInicio;

    public BairroAcompanhado() {
        super();
    }

    public BairroAcompanhado(int idUsuario, int idBairro, String motivo, LocalDateTime dtInicio) {
        super();
        this.idUsuario = idUsuario;
        this.idBairro = idBairro;
        this.motivo = motivo;
        this.dtInicio = dtInicio;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(int idBairro) {
        this.idBairro = idBairro;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(LocalDateTime dtInicio) {
        this.dtInicio = dtInicio;
    }

    @Override
    public String toString() {
        return "BairroAcompanhado{" +
                "idUsuario=" + idUsuario +
                ", idBairro=" + idBairro +
                ", motivo=" + motivo +
                ", dtInicio=" + dtInicio +
                '}';
    }
}
