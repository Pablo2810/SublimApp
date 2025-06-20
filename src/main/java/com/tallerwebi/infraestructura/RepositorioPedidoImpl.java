package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioPedidoImpl implements RepositorioPedido {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPedidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        String hql = "FROM Pedido WHERE usuarioPedido = :idUsuario";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }

    @Override
    public Boolean eliminarPedido(Long idPedido) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

        String hql = "DELETE FROM Pedido WHERE id = :id AND estado = :estado";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", idPedido);

        int cantidadDeActualizaciones = query.executeUpdate();

        if(cantidadDeActualizaciones > 0){
            tx.rollback();
            return false;
        }
        tx.commit();
        return true;
    }

    @Override
    public Boolean actualizarPedido(Pedido pedido) {

        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

        String hql = "UPDATE Pedido SET codigoPedido = :codigoPedido, fechaCreacion = :fechaCreacion, estado = :estado," +
                "montoTotal = :montoTotal, montoFinal = :montoFinal, fechaEntrega = :fechaEntrega," +
                "promocionAplicada = :promocionAplicada, usuarioPedido = :usuarioPedido, productos = :productos " +
                "WHERE id = :id ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", pedido.getId());
        query.setParameter("codigoPedido", pedido.getCodigoPedido());
        query.setParameter("fechaCreacion", pedido.getFechaCreacion());
        query.setParameter("estado", pedido.getEstado());
        query.setParameter("montoTotal", pedido.getMontoTotal());
        query.setParameter("montoFinal", pedido.getMontoFinal());
        query.setParameter("fechaEntrega", pedido.getFechaEntrega());
        query.setParameter("promocionAplicada", pedido.getPromocionAplicada());
        query.setParameter("usuarioPedido", pedido.getUsuarioPedido());
        query.setParameter("productos", pedido.getProductos());
        int cantidadDeActualizaciones = query.executeUpdate();

        if(cantidadDeActualizaciones > 0){
            tx.rollback();
            return false;
        }
        tx.commit();
        return true;
    }

    @Override
    public Boolean guardarPedido(Pedido pedido) {
        sessionFactory.getCurrentSession().save(pedido);
        return true;
    }
}
