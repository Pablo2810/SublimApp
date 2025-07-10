package com.tallerwebi.dominio.excepcion;

public class CompraTelaNoEncontrada extends RuntimeException {

    public CompraTelaNoEncontrada() {
        super("Compra de tela no encontrada.");
    }

    public CompraTelaNoEncontrada(String mensaje) {
        super(mensaje);
    }
}
