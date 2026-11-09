package br.com.fiap.rotaseca.entities;

import java.time.LocalDateTime;

/**
 * Cidadão, agente da Defesa Civil ou administrador.
 * Representa a tabela USUARIO do banco Oracle.
 */
public class Usuario {

    private int idUsuario;
    private String nomeExibicao;
    private String telefone;
    private String tipoPerfil;
    private Integer idBairro;
    private LocalDateTime dtCadastro;
    private String stAtivo;

    public Usuario() {
        super();
    }

    public Usuario(int idUsuario, String nomeExibicao, String telefone, String tipoPerfil, Integer idBairro, LocalDateTime dtCadastro, String stAtivo) {
        super();
        this.idUsuario = idUsuario;
        this.nomeExibicao = nomeExibicao;
        this.telefone = telefone;
        this.tipoPerfil = tipoPerfil;
        this.idBairro = idBairro;
        this.dtCadastro = dtCadastro;
        this.stAtivo = stAtivo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Integer getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Integer idBairro) {
        this.idBairro = idBairro;
    }

    public LocalDateTime getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDateTime dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public String getStAtivo() {
        return stAtivo;
    }

    public void setStAtivo(String stAtivo) {
        this.stAtivo = stAtivo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nomeExibicao=" + nomeExibicao +
                ", telefone=" + telefone +
                ", tipoPerfil=" + tipoPerfil +
                ", idBairro=" + idBairro +
                ", dtCadastro=" + dtCadastro +
                ", stAtivo=" + stAtivo +
                '}';
    }
}
