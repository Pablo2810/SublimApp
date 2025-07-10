package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.CompraTela;
import com.tallerwebi.dominio.entidad.EstadoTela;
import com.tallerwebi.dominio.repositorio.RepositorioCompraTela;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepositorioCompraTelaImpl implements RepositorioCompraTela {

    private final SessionFactory sessionFactory;

    public RepositorioCompraTelaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarTela(CompraTela compra) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(compra);
        System.out.println("ID generado: " + compra.getId());
        session.flush();

    }

    @Override
    public CompraTela obtenerCompraPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(CompraTela.class, id);
    }

    @Override
    public void eliminarCompra(CompraTela compra) {
        Session session = sessionFactory.getCurrentSession();
        CompraTela compraGestionada = session.get(CompraTela.class, compra.getId());
        if (compraGestionada != null) {
            session.delete(compraGestionada);
            session.flush();
        }
    }

    @Override
    public List<CompraTela> buscarComprasPorUsuarioYEstado(Long usuarioId, EstadoTela estado) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "FROM CompraTela c WHERE c.usuario.id = :usuarioId";

        if (estado != null) {
            hql += " AND c.estado = :estado";
        }

        Query<CompraTela> query = session.createQuery(hql, CompraTela.class);
        query.setParameter("usuarioId", usuarioId);

        if (estado != null) {
            query.setParameter("estado", estado);
        }

        return query.getResultList();
    }

    @Override
    public List<CompraTela> buscarComprasPorEstados(List<EstadoTela> estados) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM CompraTela c WHERE c.estado IN :estados ORDER BY c.fechaCompra DESC";
        Query<CompraTela> query = session.createQuery(hql, CompraTela.class);
        query.setParameter("estados", estados);
        return query.list();
    }

    @Override
    public List<CompraTela> buscarComprasPorUsuarioYEstados(Long usuarioId, List<EstadoTela> estados) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM CompraTela c WHERE c.usuario.id = :usuarioId AND c.estado IN :estados ORDER BY c.fechaCompra DESC";
        Query<CompraTela> query = session.createQuery(hql, CompraTela.class);
        query.setParameter("usuarioId", usuarioId);
        query.setParameter("estados", estados);
        return query.list();
    }


}

