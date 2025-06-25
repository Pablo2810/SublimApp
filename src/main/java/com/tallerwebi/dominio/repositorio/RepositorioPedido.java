package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioPedido {
    List<Pedido> listarPedidosDelUsuario(Long idUsuario);
    List<Pedido> listarPedidos();
    Pedido obtenerPedido(Long id);
    Boolean eliminarPedido(Long idPedido);
    Boolean actualizarPedido(Pedido pedido);
    Boolean guardarPedido(Pedido pedido);
    void cambiarEstadoPedido(Pedido pedido, Estado nuevoEstado);
}
