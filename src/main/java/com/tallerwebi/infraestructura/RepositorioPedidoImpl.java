package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class RepositorioPedidoImpl implements RepositorioPedido {

    private List<Pedido> pedidos;
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPedidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        Pedido pedido1 = new Pedido();
        pedido1.setId(1L);
        //pedido1.setCantCopias(10);
        //pedido1.setMetrosTotales("100.0");
        //pedido1.setCostoServicio(125.0);
        pedido1.setEstado(Estado.A_RETIRAR);
        pedido1.setFechaCreacion(LocalDate.now());
        Pedido pedido2 = new Pedido();
        pedido2.setId(2L);
        //pedido2.setCantCopias(5);
        //pedido2.setMetrosTotales("11200.0");
        //pedido2.setCostoServicio(2035.0);
        pedido2.setEstado(Estado.EN_ESPERA);
        pedido2.setFechaCreacion(LocalDate.now());
        Pedido pedido3 = new Pedido();
        pedido3.setId(3L);
        //pedido3.setCantCopias(21);
        //pedido3.setMetrosTotales("2300.0");
        //pedido3.setCostoServicio(3000.0);
        pedido3.setEstado(Estado.SUBLIMANDO);
        pedido3.setFechaCreacion(LocalDate.now());

        this.pedidos = List.of(pedido1, pedido2, pedido3);
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return this.pedidos;
    }
}
