package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ServicioPedidoTest {

    @Test
    public void queSePuedaRegistrarUnPedido() {
        ServicioPedidoImpl servicioPedido = new ServicioPedidoImpl();
        Archivo archivo = new Archivo();
        archivo.setAncho(5.0);
        archivo.setAlto(9.0); // -> alto * 1500 = 13500
        Integer cantidadCopias = 4;

        Pedido pedido = servicioPedido.registrarPedido(cantidadCopias, archivo);

        //assertThat(pedido.getCantCopias().toString(), equalToIgnoringCase("4"));
        //assertThat(pedido.getMetrosTotales(), equalToIgnoringCase("5.0x36.0"));
        //assertThat(pedido.getCostoServicio().toString(), equalToIgnoringCase("54000.0"));
        assertThat(pedido.getEstado().toString(), equalToIgnoringCase("EN_ESPERA"));
    }

    @Test
    public void queSePuedaCalcularElCostoDeServicioDeUnPedido(){
        ServicioPedidoImpl servicioPedido = new ServicioPedidoImpl();
        Archivo archivo = new Archivo();
        archivo.setAncho(5.0);
        archivo.setAlto(14.8); // -> alto * 1500
        Integer cantidadCopias = 4;

        Double costo = servicioPedido.calcularCostoTotal(archivo.getAlto(), cantidadCopias);

        assertThat(costo.toString(), equalToIgnoringCase("88800.0"));
    }
}
