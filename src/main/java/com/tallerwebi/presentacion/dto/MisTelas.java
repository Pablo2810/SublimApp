package com.tallerwebi.presentacion.dto;

import com.tallerwebi.dominio.entidad.TipoTela;

public class MisTelas {

    public MisTelas(Long id, TipoTela tipoTela, String color, Double precio, String imagenUrl) {
        this.id = id;
        this.tipoTela = tipoTela;
        this.color = color;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    private Long id;
    private TipoTela tipoTela;
    private String color;
    private Double precio;
    private String imagenUrl;
    private Double metros;

    // Getters y setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public TipoTela getTipoTela() { return tipoTela; }

    public void setTipoTela(TipoTela tipoTela) { this.tipoTela = tipoTela; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public Double getPrecio() { return precio; }

    public void setPrecio(Double precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }

    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Double getMetros() { return metros; }

    public void setMetros(Double metros) { this.metros = metros; }

}
