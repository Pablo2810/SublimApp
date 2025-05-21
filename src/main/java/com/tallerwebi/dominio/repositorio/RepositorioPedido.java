package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Pedido;

import java.util.List;

public interface RepositorioPedido {
    List<Pedido> listarPedidosDelUsuario(Long idUsuario);
}
