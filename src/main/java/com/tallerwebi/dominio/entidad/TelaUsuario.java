package com.tallerwebi.dominio.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TelaUsuario extends Tela {

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    private Usuario usuario;

    @Column(name = "es_manual")
    private Boolean esManual;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean isEsManual() {
        return esManual;
    }

    public void setEsManual(Boolean esManual) {
        this.esManual = esManual;
    }

    public Boolean getEsManual(){ return esManual; }

}