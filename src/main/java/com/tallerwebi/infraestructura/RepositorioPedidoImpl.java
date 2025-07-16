package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Estado;
import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioPedido;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RepositorioPedidoImpl implements RepositorioPedido {

    @Autowired
    private SessionFactory sessionFactory;

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
        sessionFactory.getCurrentSession().merge(pedido);
    }

    @Override
    public List<Pedido> listarPedidosDelUsuario(Long idUsuario) {
        return (List<Pedido>) sessionFactory.getCurrentSession()
                .createQuery("FROM Pedido p JOIN FETCH p.productos WHERE p.usuarioPedido.id = :usuarioBuscado", Pedido.class) // Added JOIN FETCH and Pedido.class for type safety
                .setParameter("usuarioBuscado", idUsuario)
                .getResultList();
    }

    @Override
    public List<Pedido> listarPedidosDelUsuarioNoPendiente(Long idUsuario) {
        return (List<Pedido>) sessionFactory.getCurrentSession()
                .createQuery("FROM Pedido p JOIN FETCH p.productos WHERE p.usuarioPedido.id = :usuarioBuscado AND p.estado <> :estado", Pedido.class) // Added JOIN FETCH and Pedido.class for type safety
                .setParameter("usuarioBuscado", idUsuario)
                .setParameter("estado", Estado.PENDIENTE)
                .getResultList();
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
        sessionFactory.getCurrentSession().merge(pedido);
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return (Pedido) sessionFactory.getCurrentSession()
                .createCriteria(Pedido.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}
