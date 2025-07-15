package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class ServicioPedidoTest {

    @Mock
    RepositorioPedido repositorioPedido;

    @Mock
    RepositorioProducto repositorioProducto;

    @Mock
    RepositorioTela repositorioTela;

    @InjectMocks
    ServicioPedidoImpl servicioPedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void eliminarProductoDelPedido_deberiaEliminarProductoYActualizarStock() {
        // Crear tela con metros iniciales
        Tela tela = new Tela();
        tela.setMetros(1000.0);

        // Crear talle con metrosTotales
        Talle talle = new Talle();
        talle.setMetrosTotales(2.0);

        // Producto a eliminar: cantidad 3, con tela y talle
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCantidad(3);
        producto.setTela(tela);
        producto.setTalle(talle);

        // Pedido con set de productos que incluye el producto a eliminar
        Pedido pedido = new Pedido();
        Set<Producto> productos = new HashSet<>();
        productos.add(producto);
        pedido.setProductos(productos);

        // Ejecutar método
        servicioPedido.eliminarProductoDelPedido(pedido, 1L);

        // Verificar que el producto fue removido del pedido
        assertFalse(pedido.getProductos().contains(producto));

        // Verificar que se actualizó stock (1000 + 2 * 3 = 1006)
        assertEquals(1006.0, tela.getMetros());

        // Verificar que repositorios se llamaron con los métodos correctos
        verify(repositorioTela).actualizar(tela);
        verify(repositorioPedido).actualizar(pedido);
        verify(repositorioProducto).eliminarProducto(1L);
    }


    @Test
    void eliminarProductoDelPedido_pedidoNull_deberiaLanzarIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.eliminarProductoDelPedido(null, 1L);
        });
        assertEquals("Pedido no válido o sin productos.", thrown.getMessage());
    }

    @Test
    void eliminarProductoDelPedido_productosNull_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        pedido.setProductos(null);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.eliminarProductoDelPedido(pedido, 1L);
        });
        assertEquals("Pedido no válido o sin productos.", thrown.getMessage());
    }

    @Test
    void eliminarProductoDelPedido_productoNoEncontrado_deberiaLanzarIllegalArgumentException() {
        Pedido pedido = new Pedido();
        pedido.setProductos(new HashSet<>());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.eliminarProductoDelPedido(pedido, 1L);
        });
        assertEquals("Producto no encontrado en el pedido", thrown.getMessage());
    }

    @Test
    void eliminarProductoDelPedido_errorRepositorioDeberiaLanzarRuntimeException() {
        Tela tela = new Tela();
        tela.setMetros(1000.0);

        Talle talle = new Talle();
        talle.setMetrosTotales(2.0);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCantidad(3);
        producto.setTela(tela);
        producto.setTalle(talle);

        Pedido pedido = new Pedido();
        Set<Producto> productos = new HashSet<>();
        productos.add(producto);
        pedido.setProductos(productos);

        // Forzar excepción en repositorioTela.actualizar
        doThrow(new RuntimeException("DB error")).when(repositorioTela).actualizar(any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            servicioPedido.eliminarProductoDelPedido(pedido, 1L);
        });

        assertTrue(thrown.getMessage().contains("Error al eliminar el producto del pedido"));
        verify(repositorioTela).actualizar(tela);
        // No debería llegar a eliminar el producto ni actualizar pedido si falla la actualización de tela
        verify(repositorioPedido, never()).actualizar(any());
        verify(repositorioProducto, never()).eliminarProducto(anyLong());
    }

    @Test
    void eliminarProductoDelPedido_multiplesProductos_eliminaSoloElCorrecto() {
        Tela tela1 = new Tela();
        tela1.setMetros(1000.0);

        Tela tela2 = new Tela();
        tela2.setMetros(500.0);

        Talle talle = new Talle();
        talle.setMetrosTotales(2.0);

        Producto producto1 = new Producto();
        producto1.setId(1L);
        producto1.setCantidad(1);
        producto1.setTela(tela1);
        producto1.setTalle(talle);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setCantidad(2);
        producto2.setTela(tela2);
        producto2.setTalle(talle);

        Pedido pedido = new Pedido();
        Set<Producto> productos = new HashSet<>();
        productos.add(producto1);
        productos.add(producto2);
        pedido.setProductos(productos);

        servicioPedido.eliminarProductoDelPedido(pedido, 1L);

        // Producto 1 eliminado, producto 2 queda
        assertFalse(pedido.getProductos().contains(producto1));
        assertTrue(pedido.getProductos().contains(producto2));

        // Stock actualizado solo en tela1
        assertEquals(1002.0, tela1.getMetros()); // 1000 + 2*1
        assertEquals(500.0, tela2.getMetros());

        verify(repositorioTela).actualizar(tela1);
        verify(repositorioPedido).actualizar(pedido);
        verify(repositorioProducto).eliminarProducto(1L);
    }

    @Test
    public void asociarProductoPedido_deberiaCalcularMontoTotalYActualizarPedido() {
        Producto p1 = new Producto();
        p1.setPrecio(100.0);
        p1.setCantidad(2); // 200

        Producto p2 = new Producto();
        p2.setPrecio(50.0);
        p2.setCantidad(3); // 150

        Pedido pedido = new Pedido();
        pedido.setProductos(new HashSet<>(Arrays.asList(p1, p2)));


        servicioPedido.asociarProductoPedido(pedido);

        assertEquals(350.0, pedido.getMontoTotal(), 0.001);
        verify(repositorioPedido).actualizar(pedido);
    }

    @Test
    void asociarProductoPedido_deberiaLanzarExcepcionSiPedidoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.asociarProductoPedido(null);
        });
    }

    @Test
    void asociarProductoPedido_deberiaLanzarExcepcionSiListaDeProductosEsNull() {
        Pedido pedido = new Pedido();
        pedido.setProductos(null);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.asociarProductoPedido(pedido);
        });
    }

    @Test
    void asociarProductoPedido_deberiaLanzarExcepcionSiUnProductoTienePrecioNull() {
        Producto producto = new Producto();
        producto.setPrecio(null);
        producto.setCantidad(1);

        Pedido pedido = new Pedido();
        pedido.setProductos(Set.of(producto));

        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.asociarProductoPedido(pedido);
        });
    }

}