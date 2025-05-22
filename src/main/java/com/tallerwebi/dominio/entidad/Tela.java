package com.tallerwebi.dominio.entidad;

public class Tela {
    private Long id;
    private TipoTela tipoTela;
    private Double metros;
    private String color;
    private Double precio;
    private String imagenUrl;
    private Boolean esPersonalizada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public Double getPrecio() { return precio; }

    public void setPrecio(Double precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }

    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Boolean getEsPersonalizada() { return esPersonalizada; }

    public void setEsPersonalizada(Boolean esPersonalizada) { this.esPersonalizada = esPersonalizada; }
}
