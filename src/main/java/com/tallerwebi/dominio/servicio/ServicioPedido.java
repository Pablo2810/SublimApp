package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Transactional
public interface ServicioPedido {
//    Pedido registrarPedido(String codigoPedido, Usuario usuario, HashSet<Producto> productos);
    Double calcularCostoTotal(Pedido pedido, double cotizacion);
//    Pedido registrarPedidoConDescuento(String codigoPedido, Usuario usuario, HashSet<Producto> productos, Promocion promocion);
    List<Pedido> listarPedidosDelUsuario(Long idUsuario);
    List<Pedido> listarPedidos();
    Pedido obtenerPedido(Long id);
    boolean cambiarEstadoPedido(Long id, Estado nuevoEstado);
    void aplicarPromocion(Pedido pedido, Promocion promocion);


    Pedido buscarPedidoEstadoPendiente(Usuario usuario);
    void asociarProductoPedido(Pedido pedido);

    List<Pedido> listarPedidosDelUsuarioNoPendiente(Long id);

    void generarPedidoCompleto(Long id, String tipoMonedaPago, double cotizacion, String codigoPedido, LocalDate fechaCreacion, int diasEspera);

    void cancelarPedido(Long id);
    void eliminarProductoDelPedido(Pedido pedido, Long productoId);

    Pedido buscarPendiente(Usuario usuario);

    void eliminarPedido(Pedido pedidoEncontrado);

    @Transactional
    Pedido pagarPedido(Long pedidoId, Moneda moneda);

    @Transactional
    Pedido procesarPagoPedidoProductos(Long pedidoId,
                                       boolean pagoEnDolares,
                                       String metodoPago,
                                       String opcionEnvioStr,
                                       String direccionEnvio,
                                       String numeroTarjeta,
                                       String cvv,
                                       String nombreTitular,
                                       String vencimiento,
                                       Integer cuotas);
}
