package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Transactional
public interface ServicioPedido {
    Pedido registrarPedido(String codigoPedido, Usuario usuario, HashSet<Producto> productos);
    Double calcularCostoTotal(Pedido pedido);
    Pedido registrarPedidoConDescuento(String codigoPedido, Usuario usuario, HashSet<Producto> productos, Promocion promocion);
    List<Pedido> listarPedidosDelUsuario(Long idUsuario);
    List<Pedido> listarPedidos();
    Pedido obtenerPedido(Long id);
    boolean cambiarEstadoPedido(Long id, Estado nuevoEstado);
    void aplicarPromocion(Pedido pedido, Promocion promocion);


    Pedido buscarPedidoEstadoPendiente(Usuario usuario);
    void asociarProductoPedido(Pedido pedido);
}
