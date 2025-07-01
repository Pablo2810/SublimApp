package com.tallerwebi.presentacion.dto;

public class DatosPrenda {
    private Long id;
    private String descripcion;
    private Double precioBase;

    public DatosPrenda (Long id, String descripcion, Double precioBase) {
        this.id = id;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
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
    public Double getPrecioBase() {
        return precioBase;
    }
    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }
}
