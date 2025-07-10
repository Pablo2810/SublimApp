package com.tallerwebi.presentacion.dto;

public class DatosPais {
    private String nombre;
    private String banderaUrl;

    public DatosPais() {}

    public DatosPais(String nombre, String banderaUrl) {
        this.nombre = nombre;
        this.banderaUrl = banderaUrl;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getBanderaUrl() { return banderaUrl; }
    public void setBanderaUrl(String banderaUrl) { this.banderaUrl = banderaUrl; }
}
