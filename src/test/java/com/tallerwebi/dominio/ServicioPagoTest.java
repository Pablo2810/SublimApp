package com.tallerwebi.dominio;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioPagoTest {

    private ServicioPagoImpl servicioPago;

    @BeforeEach
    public void setUp() {
        servicioPago = new ServicioPagoImpl();
    }

    @Test
    public void calcularTotal_SinInteres_Cuotas1() {
        double precio = 100;
        double metros = 2;
        double costoEnvio = 50;
        int cuotas = 1;

        double esperado = (precio * metros + costoEnvio) * (1 + 0.0);
        double resultado = servicioPago.calcularTotal(precio, metros, costoEnvio, cuotas);

        assertEquals(esperado, resultado, 0.0001);
    }

    @Test
    public void calcularTotal_ConInteres_Cuotas6() {
        double precio = 200;
        double metros = 1.5;
        double costoEnvio = 30;
        int cuotas = 6;

        double interes = 0.12; // para 6 cuotas según el método
        double esperado = (precio * metros + costoEnvio) * (1 + interes);
        double resultado = servicioPago.calcularTotal(precio, metros, costoEnvio, cuotas);

        assertEquals(esperado, resultado, 0.0001);
    }

    @Test
    public void calcularValorCuota_CalculaCorrectamente() {
        double total = 360;
        int cuotas = 6;

        double esperado = total / cuotas;
        double resultado = servicioPago.calcularValorCuota(total, cuotas);

        assertEquals(esperado, resultado, 0.0001);
    }

    @Test
    public void calcularTotalEnDolares_CotizacionValida() {
        double total = 1270;
        double cotizacionDolar = 127;

        double esperado = total / cotizacionDolar;
        double resultado = servicioPago.calcularTotalEnDolares(total, cotizacionDolar);

        assertEquals(esperado, resultado, 0.0001);
    }

    @Test
    public void calcularTotalEnDolares_CotizacionInvalida_LanzaExcepcion() {
        double total = 1000;
        double cotizacionDolar = 0;

        assertThrows(IllegalArgumentException.class, () -> {
            servicioPago.calcularTotalEnDolares(total, cotizacionDolar);
        });
    }

    @Test
    public void obtenerInteresPorCuotas_CasosEspecificos() throws Exception {
        // Para probar métodos privados, podés usar reflexión o simplemente testear los efectos indirectos
        // Aquí solo comparamos resultados de calcularTotal con cuotas conocidas

        double precio = 100;
        double metros = 1;
        double costoEnvio = 0;

        double total2 = servicioPago.calcularTotal(precio, metros, costoEnvio, 2);
        double esperado2 = (precio * metros + costoEnvio) * 1.05;
        assertEquals(esperado2, total2, 0.0001);

        double total3 = servicioPago.calcularTotal(precio, metros, costoEnvio, 3);
        double esperado3 = (precio * metros + costoEnvio) * 1.08;
        assertEquals(esperado3, total3, 0.0001);

        double total12 = servicioPago.calcularTotal(precio, metros, costoEnvio, 12);
        double esperado12 = (precio * metros + costoEnvio) * 1.20;
        assertEquals(esperado12, total12, 0.0001);
    }

    @Test
    public void calcularTotal_PrecioCero() {
        double total = servicioPago.calcularTotal(0, 10, 50, 1);
        assertEquals(50, total, 0.0001);
    }

    @Test
    public void calcularTotal_MetrosCero() {
        double total = servicioPago.calcularTotal(100, 0, 50, 1);
        assertEquals(50, total, 0.0001);
    }

    @Test
    public void calcularTotal_CostoEnvioCero() {
        double total = servicioPago.calcularTotal(100, 2, 0, 3);
        double esperado = 100 * 2 * 1.08; // 8% interés por 3 cuotas
        assertEquals(esperado, total, 0.0001);
    }

    @Test
    public void calcularTotal_CuotasNoListadas_InteresCero() {
        double total = servicioPago.calcularTotal(100, 1, 10, 4); // 4 no tiene interés asignado
        double esperado = (100 * 1 + 10);
        assertEquals(esperado, total, 0.0001);
    }

    @Test
    public void calcularValorCuota_CuotasCero_DebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPago.calcularValorCuota(100, 0);
        });
    }

    @Test
    public void calcularTotalEnDolares_CotizacionNegativa_DebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPago.calcularTotalEnDolares(100, -5);
        });
    }


}

