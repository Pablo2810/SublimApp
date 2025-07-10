package com.tallerwebi.dominio.entidad;

public enum Estado {
    PENDIENTE("Pago pendiente", "text-danger"),
    EN_ESPERA("En espera", "text-primary"),
    SUBLIMANDO("Sublimado", "text-warning"),
    A_RETIRAR("Listo para retirar", "text-success"),
    CANCELADO("Cancelado", "text-danger");

    private final String descripcion;
    private final String claseCss;

    Estado(String descripcion, String claseCss) {
        this.descripcion = descripcion;
        this.claseCss = claseCss;
    }

    public String getClaseCss() {
        return this.claseCss;
    }
    public String getDescripcion() {
        return descripcion;
    }
}