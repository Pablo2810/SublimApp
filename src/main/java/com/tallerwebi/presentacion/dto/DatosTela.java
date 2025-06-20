package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.entidad.TipoTela;

public class DatosTela {
    private long id;
    private TipoTela tipoTela;
    private Double metros;
    private String color;
    private Double precio;
    private String imagenUrl;

    public DatosTela(TipoTela tipoTela, Double metros, String color, Double precio, String imagenUrl) {
        this.tipoTela = tipoTela;
        this.metros = metros;
        this.color = color;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
