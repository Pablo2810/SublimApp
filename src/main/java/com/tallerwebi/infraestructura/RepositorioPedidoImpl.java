package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
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
/*
    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        String hql = "FROM Pedido WHERE usuarioPedido = :idUsuario";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }

    @Override
    public List<Pedido> listarPedidos() {
        String hql = "From Pedido";
        return this.sessionFactory.getCurrentSession().createQuery(hql).getResultList();
    }

    @Override
    public Pedido obtenerPedido(Long id) {
        return this.sessionFactory.getCurrentSession().get(Pedido.class, id);
    }

    @Override
    public void cambiarEstadoPedido(Pedido pedido, Estado nuevoEstado) {
        pedido.setEstado(nuevoEstado);
        this.sessionFactory.getCurrentSession().update(pedido);
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
    }*/

    @Override
    public List<Pedido> listarPedidos() {
        String hql = "From Pedido";
        return this.sessionFactory.getCurrentSession().createQuery(hql).getResultList();
    }

    @Override
    public Pedido obtenerPedido(Long id) {
        return this.sessionFactory.getCurrentSession().get(Pedido.class, id);
    }

    @Override
    public void cambiarEstadoPedido(Pedido pedido, Estado nuevoEstado) {
        pedido.setEstado(nuevoEstado);
        this.sessionFactory.getCurrentSession().update(pedido);
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return null;
    }

    @Override
    public Boolean eliminarPedido(Long idPedido) {
        return null;
    }

    @Override
    public Boolean actualizarPedido(Pedido pedido) {
        return null;
    }

    @Override
    public Boolean guardarPedido(Pedido pedido) {
        sessionFactory.getCurrentSession().save(pedido);
        return true;
    }

    @Override
    public void guardar(Pedido pedido) { sessionFactory.getCurrentSession().save(pedido); }

    @Override
    public Pedido buscarPedidoPendientePorUsuario(Usuario usuario) {
        return (Pedido) sessionFactory.getCurrentSession()
                .createQuery("FROM Pedido p LEFT JOIN FETCH p.productos WHERE p.usuarioPedido = :usuario AND p.estado = :estado")
                .setParameter("usuario", usuario)
                .setParameter("estado", Estado.PENDIENTE)
                .uniqueResult();
    }

    @Override
    public void actualizar(Pedido pedido) {
        sessionFactory.getCurrentSession().update(pedido);
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return (Pedido) sessionFactory.getCurrentSession()
                .createCriteria(Pedido.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}
