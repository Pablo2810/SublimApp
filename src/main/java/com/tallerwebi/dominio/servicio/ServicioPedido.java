package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.presentacion.dto.DatosPedido;

public interface ServicioPedido {
    Pedido registrarPedido(Integer cantidadCopias, Archivo archivo);
}
