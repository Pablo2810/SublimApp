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
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioProductoImpl implements RepositorioProducto {

    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioProductoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Producto> listarProductosDeUnPedido(Long idPedido) {
        return null; // A implementar si se necesita
    }

    @Override
    public List<Pedido> listarProductosBase() {
        return null; // A implementar si se necesita
    }

    @Override
    @Transactional
    public void eliminarProducto(Long idProducto) {
        Producto producto = this.buscar(idProducto);

        for (Pedido pedido : producto.getPedidos()) {
            pedido.getProductos().remove(producto);
        }

        producto.getPedidos().clear();

        sessionFactory.getCurrentSession().delete(producto);
    }

    @Override
    public Boolean actualizarProducto(Producto producto) {
        return null; // A implementar si se necesita
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

