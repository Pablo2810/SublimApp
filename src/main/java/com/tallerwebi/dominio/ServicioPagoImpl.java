package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import com.tallerwebi.dominio.servicio.ServicioPago;

@Service
public class ServicioPagoImpl implements ServicioPago {

    @Override
    public double calcularTotal(double precio, double metros, double costoEnvio, int cuotas) {
        double subtotal = precio * metros;
        double totalConEnvio = subtotal + costoEnvio;
        double interes = obtenerInteresPorCuotas(cuotas);
        return totalConEnvio * (1 + interes);
    }

    @Override
    public double calcularValorCuota(double total, int cuotas) {
        if (cuotas <= 0) {
            throw new IllegalArgumentException("La cantidad de cuotas debe ser mayor a cero");
        }
        return total / cuotas;
    }

    @Override
    public double calcularTotalEnDolares(double total, double cotizacionDolar) {
        if (cotizacionDolar <= 0) throw new IllegalArgumentException("Cotización inválida");
        return total / cotizacionDolar;
    }

    private double obtenerInteresPorCuotas(int cuotas) {
        double interes;
        switch (cuotas) {
            case 2: interes = 0.05; break;
            case 3: interes = 0.08; break;
            case 6: interes = 0.12; break;
            case 12: interes = 0.20; break;
            default: interes = 0.0;
        }
        return interes;
    }

}


