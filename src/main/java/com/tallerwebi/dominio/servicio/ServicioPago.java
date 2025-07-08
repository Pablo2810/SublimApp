package com.tallerwebi.dominio.servicio;

public interface ServicioPago {

    double calcularTotal(double precio, double metros, double costoEnvio, int cuotas);
    double calcularValorCuota(double total, int cuotas);
    double calcularTotalEnDolares(double total, double cotizacionDolar);

}

