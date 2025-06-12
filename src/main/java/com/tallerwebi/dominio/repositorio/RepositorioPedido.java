package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Pedido;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioPedido {
    List<Pedido> listarPedidosDelUsuario(Long idUsuario);
    Boolean eliminarPedido(Long idPedido);
    Boolean actualizarPedido(Pedido pedido);
    Boolean guardarPedido(Pedido pedido);
}
