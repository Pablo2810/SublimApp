package com.tallerwebi.presentacion.dto;

public class DatosPedido {
    private String nombre;
    private Integer cantidadCopias;

    public DatosPedido() {  }

    public DatosPedido(String nombre, Integer cantidadCopias) {
        this.nombre = nombre;
        this.cantidadCopias = cantidadCopias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadCopias() {
        return cantidadCopias;
    }

    public void setCantidadCopias(Integer cantidadCopias) {
        this.cantidadCopias = cantidadCopias;
    }
}
