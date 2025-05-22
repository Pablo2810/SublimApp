package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface RepositorioPedido {
    List<Pedido> listarPedidosDelUsuario(Usuario usuario);
    //void agregarPedido(Pedido pedido);
}
