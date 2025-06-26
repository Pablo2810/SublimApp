package com.tallerwebi.dominio.excepcion;

public class StockInsuficiente extends RuntimeException {
    public StockInsuficiente() {
        super("No hay suficiente stock de la tela.");
    }
}