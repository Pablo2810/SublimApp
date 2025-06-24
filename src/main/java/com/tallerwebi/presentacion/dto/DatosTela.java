package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.entidad.TipoTela;

public class DatosTela {
    private Long id;
    private TipoTela tipoTela;
    private Double metros;
    private String color;
    private Double precio;
    private String imagenUrl;
    private String descripcion;

    public DatosTela(){}

    public DatosTela(TipoTela tipoTela, Double metros, String color, Double precio, String imagenUrl, String descripcion) {
        this.tipoTela = tipoTela;
        this.metros = metros;
        this.color = color;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
    }

    public DatosTela(Long id, TipoTela tipoTela, Double metros, String color, Double precio, String imagenUrl, String descripcion) {
        this.id = id;
        this.tipoTela = tipoTela;
        this.metros = metros;
        this.color = color;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
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

    public TipoTela getTipoTela() {
        return tipoTela;
    }

    public void setTipoTela(TipoTela tipoTela) {
        this.tipoTela = tipoTela;
    }

    public Double getMetros() {
        return metros;
    }

    public void setMetros(Double metros) {
        this.metros = metros;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
