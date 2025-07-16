package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import com.tallerwebi.dominio.repositorio.RepositorioProducto;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioProductoImpl implements RepositorioProducto {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioProductoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Producto> listarProductosDeUnPedido(Long idPedido) {
        /*String hql = "FROM Producto WHERE pedidos = :idPedido";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idPedido", idPedido);
        return query.getResultList();*/
        return null;
    }

    @Override
    public List<Pedido> listarProductosBase() {
        /*String hql = "FROM ProductoBase";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();*/
        return null;
    }

    @Override
    public Boolean eliminarProducto(Long idProducto) {
        /*Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

        String hql = "DELETE FROM Producto WHERE id = :idProducto AND :idPedido IN (pedidos)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idProducto", idProducto);

        int cantidadDeActualizaciones = query.executeUpdate();

        if(cantidadDeActualizaciones > 0){
            tx.rollback();
            return false;
        }
        tx.commit();
        return true;*/
        return null;
    }

    @Override
    public void actualizarProducto(Producto producto) {
        sessionFactory.getCurrentSession().saveOrUpdate(producto);
    }

    @Override
    public Producto obtenerProducto(Long id) {
        return sessionFactory.getCurrentSession().get(Producto.class, id);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        Long idGuardado = (Long) sessionFactory.getCurrentSession().save(producto);
        return sessionFactory.getCurrentSession().get(Producto.class, idGuardado);
    }
}
