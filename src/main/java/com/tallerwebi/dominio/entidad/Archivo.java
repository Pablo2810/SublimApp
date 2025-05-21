package com.tallerwebi.dominio.entidad;

import java.io.InputStream;

public class Archivo {
    private Long id;
    private String nombre;
    private String tipoFormato;
    private Double pesoMB;
    private InputStream datos;

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

    public void setDatos(InputStream datos) { this.datos = datos; }

    public InputStream getDatos() { return datos; }
}
