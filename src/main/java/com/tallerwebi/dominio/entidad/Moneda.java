package com.tallerwebi.dominio.entidad;

public enum Moneda {
    PESOS_ARGENTINOS (1, "Pesos argentinos", "ARS"),
    DOLAR (2, "Dolar", "USD"),
    EURO (3, "Euro", "EUR");

    private long id;
    private String descripcion;
    private String simbolo;

    Moneda(long id, String descripcion, String simbolo) {
        this.id = id;
        this.descripcion = descripcion;
        this.simbolo = simbolo;
    }

    public long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
