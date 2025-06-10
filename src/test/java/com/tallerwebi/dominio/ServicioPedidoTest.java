package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.servicio.ServicioPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ServicioPedidoTest {

    private RepositorioPedido repositorioPedido;
    private ServicioPedido servicioPedido;
    private Usuario usuario;
    private HashSet<Producto> productos;

    @BeforeEach
    public void init() {
        repositorioPedido = mock(RepositorioPedido.class);
        servicioPedido = new ServicioPedidoImpl(repositorioPedido);
        usuario = mock(Usuario.class);
        Producto producto1 = mock(Producto.class);
        when(producto1.getPrecio()).thenReturn(5000.00);
        Producto producto2 = mock(Producto.class);
        when(producto2.getPrecio()).thenReturn(2000.00);
        Producto producto3 = mock(Producto.class);
        when(producto3.getPrecio()).thenReturn(3000.00);
        productos.add(producto1);
        productos.add(producto2);
        productos.add(producto3);
    }

    @Test
    public void queSePuedaIngresarUnPedidoCuandoLosDatosSonValidos() {
        Pedido pedido = servicioPedido.registrarPedido("P-0000000001",usuario,productos);

        assertThat(pedido.getMontoFinal().toString(), equalToIgnoringCase("10000.00"));
        assertThat(pedido.getEstado().toString(), equalToIgnoringCase("EN_ESPERA"));
    }

    @Test
    public void queSePuedaRegistrarUnPedido() {
        ServicioPedidoImpl servicioPedido = new ServicioPedidoImpl(repositorioPedido);
        Archivo archivo = new Archivo();
        archivo.setAncho(5.0);
        archivo.setAlto(9.0); // -> alto * 1500 = 13500
        Integer cantidadCopias = 4;

        Pedido pedido = servicioPedido.registrarPedido("P-0000000001",usuario,productos);

        //assertThat(pedido.getCantCopias().toString(), equalToIgnoringCase("4"));
        //assertThat(pedido.getMetrosTotales(), equalToIgnoringCase("5.0x36.0"));
        //assertThat(pedido.getCostoServicio().toString(), equalToIgnoringCase("54000.0"));
        assertThat(pedido.getEstado().toString(), equalToIgnoringCase("EN_ESPERA"));
    }

    @Test
    public void queSePuedaCalcularElCostoDeServicioDeUnPedido(){
        ServicioPedidoImpl servicioPedido = new ServicioPedidoImpl(repositorioPedido);
        Archivo archivo = new Archivo();
        archivo.setAncho(5.0);
        archivo.setAlto(14.8); // -> alto * 1500
        Integer cantidadCopias = 4;

        Double costo = servicioPedido.calcularCostoTotal(archivo.getAlto(), cantidadCopias);

        assertThat(costo.toString(), equalToIgnoringCase("88800.0"));
    }
}
