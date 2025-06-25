package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.entidad.Estado;

public class DatosModificarPedido {
    private Estado estado;

    public DatosModificarPedido(Estado estado) {
        this.estado = estado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
