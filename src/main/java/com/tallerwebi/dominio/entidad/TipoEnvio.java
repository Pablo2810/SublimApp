package com.tallerwebi.dominio.entidad;

public enum TipoEnvio {

    LOCAL(0.0, "Retiro en local (gratis)", "1 a 2 días"),
    CABA(1000.0, "Envío a CABA ($1000)", "2 a 4 días hábiles"),
    INTERIOR(2500.0, "Envío al interior ($2500)", "4 a 7 días hábiles"),
    EXTERIOR(10000.0, "Envío fuera del país ($10000)", "15 a 30 días hábiles");

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

    @Override
    public String toString() {
        return descripcion;
    }
}



