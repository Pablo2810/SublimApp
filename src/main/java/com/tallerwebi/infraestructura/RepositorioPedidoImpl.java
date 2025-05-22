package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import com.tallerwebi.dominio.util.LogHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
public class RepositorioPedidoImpl implements RepositorioPedido {

    private List<Pedido> pedidos;
    private SessionFactory sessionFactory;

    public RepositorioPedidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        Pedido pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setCantCopias(10);
        pedido1.setMetrosTotales("10x10");
        pedido1.setCostoServicio(125.0);
        pedido1.setEstado(Estado.A_RETIRAR);
        pedido1.setFechaCreacion(LocalDate.now());

        this.pedidos = List.of(pedido1);
    }

//    @Override
//    public void agregarPedido(Pedido pedido) {
//        this.sessionFactory.getCurrentSession().save(pedido);
//    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Usuario usuario) {

        //return (List<Pedido>) sessionFactory.getCurrentSession().createCriteria(Pedido.class).add(Restrictions.eq("usuario", usuario)).list();

        return this.pedidos;
    }
}
