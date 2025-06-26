package com.tallerwebi.dominio.excepcion;

public class TelaNoEncontrada extends RuntimeException {
    public TelaNoEncontrada() {
        super("La tela no fue encontrada.");
    }
}

