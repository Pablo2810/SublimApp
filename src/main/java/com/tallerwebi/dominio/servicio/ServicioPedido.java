package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Archivo;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.presentacion.dto.DatosPedido;

import java.io.IOException;

public interface ServicioPedido {
    Pedido registrarPedido(Integer cantidadCopias, Archivo archivo);
    String calcularMetros(Pedido pedido) throws IOException;
    Double calcularCostoTotal(Double alto);
    Double aplicarDescuento();
}
