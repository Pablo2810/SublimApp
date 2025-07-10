package com.tallerwebi.presentacion.dto;

public class DatosTalle {
    private Long id;
    private String descripcion;
    private Double cinturaMIN;
    private Double cinturaMAX;
    private Double pechoMIN;
    private Double pechoMAX;
    private Double metrosTotales;

    public DatosTalle(){

    }

    public DatosTalle(Long id, String descripcion, Double cinturaMIN, Double cinturaMAX, Double pechoMIN, Double pechoMAX, Double metrosTotales) {
        this.descripcion = descripcion;
        this.cinturaMIN = cinturaMIN;
        this.cinturaMAX = cinturaMAX;
        this.pechoMIN = pechoMIN;
        this.pechoMAX = pechoMAX;
        this.metrosTotales = metrosTotales;
    }

    public DatosTalle(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public DatosTalle(Long id, String descripcion, Double metrosTotales) {
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

    public Double getCinturaMIN() {
        return cinturaMIN;
    }

    public void setCinturaMIN(Double cinturaMIN) {
        this.cinturaMIN = cinturaMIN;
    }

    public Double getCinturaMAX() {
        return cinturaMAX;
    }

    public void setCinturaMAX(Double cinturaMAX) {
        this.cinturaMAX = cinturaMAX;
    }

    public Double getPechoMIN() {
        return pechoMIN;
    }

    public void setPechoMIN(Double pechoMIN) {
        this.pechoMIN = pechoMIN;
    }

    public Double getPechoMAX() {
        return pechoMAX;
    }

    public void setPechoMAX(Double pechoMAX) {
        this.pechoMAX = pechoMAX;
    }

    public Double getMetrosTotales() {
        return metrosTotales;
    }

    public void setMetrosTotales(Double metrosTotales) {
        this.metrosTotales = metrosTotales;
    }
}
