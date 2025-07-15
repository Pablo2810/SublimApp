package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Pedido;
import com.tallerwebi.dominio.entidad.Producto;
import com.tallerwebi.dominio.entidad.Talle;
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
        return null;
    }

    @Override
    public List<Pedido> listarProductosBase() {
        return null;
    }

    @Override
    public Boolean eliminarProducto(Long idProducto) {
        Producto producto = sessionFactory.getCurrentSession().get(Producto.class, idProducto);
        if (producto != null) {
            sessionFactory.getCurrentSession().delete(producto);
            return true;
        }
        return false;
    }

    @Override
    public Boolean actualizarProducto(Producto producto) {
        return null;
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        Long idGuardado = (Long) sessionFactory.getCurrentSession().save(producto);
        return sessionFactory.getCurrentSession().get(Producto.class, idGuardado);
    }

    @Override
    public Producto buscar(Long id) {
        return sessionFactory.getCurrentSession().get(Producto.class, id);
    }
}
