package com.tallerwebi.dominio.excepcion;

public class CancelacionNoPermitida extends RuntimeException {

    public CancelacionNoPermitida() {
        super("Cancelación no permitida.");
    }

    public CancelacionNoPermitida(String mensaje) {
        super(mensaje);
    }
}