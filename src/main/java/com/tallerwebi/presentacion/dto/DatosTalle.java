package com.tallerwebi.presentacion.dto;

public class DatosTalle {
    private long id;
    private String descripcion;
    private Double metrosTotales;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMetrosTotales() {
        return metrosTotales;
    }

    public void setMetrosTotales(Double metrosTotales) {
        this.metrosTotales = metrosTotales;
    }
}
