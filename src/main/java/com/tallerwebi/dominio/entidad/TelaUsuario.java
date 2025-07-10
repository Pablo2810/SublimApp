package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class TelaUsuario extends Tela {

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoTela estado;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    private String direccionEntrega;


    public EstadoTela getEstado() {
        return estado;
    }

    public void setEstado(EstadoTela estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}