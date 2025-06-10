package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.dto.DatosPedido;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Transactional
public interface ServicioPedido {
    Pedido registrarPedido(String codigoPedido, Usuario usuario, HashSet<Producto> productos);
    Double calcularCostoTotal(Double alto, Integer cantidadCopias);
    Double aplicarDescuento();
    List<Pedido> listarPedidosDelUsuario(Long idUsuario);
}
