package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RepositorioPedidoImpl implements RepositorioPedido {

    private List<Pedido> pedidos;

    public RepositorioPedidoImpl() {
        Pedido pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setCantCopias(10);
        pedido1.setMetrosTotales("100.0");
        pedido1.setCostoServicio(125.0);
        pedido1.setEstado(Estado.A_RETIRAR);
        pedido1.setFechaCreacion(LocalDate.now());

        this.pedidos = List.of(pedido1);
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return this.pedidos;
    }
}
