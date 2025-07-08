package com.tallerwebi.dominio.excepcion;

public class TelaUsuarioNoEncontrada extends RuntimeException {

    public TelaUsuarioNoEncontrada() {
        super("Tela del usuario no encontrada.");
    }

    public TelaUsuarioNoEncontrada(String mensaje) {
        super(mensaje);
    }
}

