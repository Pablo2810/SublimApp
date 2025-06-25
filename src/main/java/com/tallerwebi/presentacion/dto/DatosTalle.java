package com.tallerwebi.presentacion.dto;

public class DatosTalle {
    private Long id;
    private String descripcion;
    private Double metrosTotales;

    public DatosTalle(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public DatosTalle(Long id, String descripcion, Double metrosTotales) {
        this.id = id;
        this.descripcion = descripcion;
        this.metrosTotales = metrosTotales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
