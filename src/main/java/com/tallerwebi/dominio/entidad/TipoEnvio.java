package com.tallerwebi.dominio.entidad;

public enum TipoEnvio {
    LOCAL(0.0, "Retiro en local (gratis)", "1 a 2 días"),
    CABA(1000.0, "Envío a CABA ($1000)", "2 a 4 días hábiles"),
    INTERIOR(2000.0, "Envío al interior ($2000)", "4 a 7 días hábiles");

    private final double costo;
    private final String descripcion;
    private final String tiempoEntrega;

    TipoEnvio(double costo, String descripcion, String tiempoEntrega) {
        this.costo = costo;
        this.descripcion = descripcion;
        this.tiempoEntrega = tiempoEntrega;
    }

    public double getCosto() {
        return costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTiempoEntrega() {
        return tiempoEntrega;
    }
}


