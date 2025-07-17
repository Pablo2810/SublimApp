package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
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



    void guardar(Pedido pedido);
    Pedido buscarPedidoPendientePorUsuario(Usuario usuario);
    void actualizar(Pedido pedido);
    Pedido buscarPorId(Long id);
    void cambiarEstadoPedido(Pedido pedido, Estado nuevoEstado);

    List<Pedido> listarPedidosDelUsuarioNoPendiente(Long idUsuario);

    void eliminar(Pedido pedidoEncontrado);

}
