package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioPedido {
    Pedido registrarPedido(Integer cantidadCopias, Archivo archivo, Usuario usuario);
    Double calcularCostoTotal(Double alto, Integer cantidadCopias);
    Double aplicarDescuento();
    List<Pedido> listarPedidosDelUsuario(Usuario usuario);
}
