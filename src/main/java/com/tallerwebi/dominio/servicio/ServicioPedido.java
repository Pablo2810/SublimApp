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

    void generarPedidoCompleto(Long id, Moneda monedaDePago, double cotizacion, String codigoPedido, LocalDate fechaCreacion, int diasEspera);
    void cancelarPedido(Long id);
    void eliminarProductoDelPedido(Pedido pedido, Long productoId);

    Pedido buscarPendiente(Usuario usuario);

    void eliminarPedido(Pedido pedidoEncontrado);
}
