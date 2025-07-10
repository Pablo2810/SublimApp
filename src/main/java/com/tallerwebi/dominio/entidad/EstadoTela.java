package com.tallerwebi.dominio.entidad;

public enum EstadoTela {

    EN_DEPOSITO("En depósito", "text-secondary"),
    EN_VIAJE("En viaje", "text-info"),
    ENTREGADO("Entregado", "text-success");

    private final String descripcion;
    private final String claseCss;

    EstadoTela(String descripcion, String claseCss) {
        this.descripcion = descripcion;
        this.claseCss = claseCss;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getClaseCss() {
        return claseCss;
    }
}
