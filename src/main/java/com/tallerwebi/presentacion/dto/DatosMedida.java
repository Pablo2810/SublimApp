package com.tallerwebi.presentacion.dto;

public class DatosMedida {
    private Double cintura;
    private Double pecho;
    private String preferencia;
    private String unidad;
    private String pais;

    public Double getCintura() { return cintura; }

    public void setCintura(Double cintura) {
        this.cintura = cintura;
    }

    public Double getPecho() {
        return pecho;
    }

    public void setPecho(Double pecho) {
        this.pecho = pecho;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
