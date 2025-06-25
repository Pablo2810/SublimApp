package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String tipoFormato;

    private Double pesoMB;

    private Double ancho;

    private Double alto;

    @OneToOne(mappedBy = "archivo")
    private Producto producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoFormato() {
        return tipoFormato;
    }

    public void setTipoFormato(String tipoFormato) {
        this.tipoFormato = tipoFormato;
    }

    public Double getPesoMB() {
        return pesoMB;
    }

    public void setPesoMB(Double pesoMB) {
        this.pesoMB = pesoMB;
    }

    public Double getAncho() { return ancho; }

    public void setAncho(Double ancho) { this.ancho = ancho; }

    public Double getAlto() { return alto; }

    public void setAlto(Double alto) { this.alto = alto; }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}