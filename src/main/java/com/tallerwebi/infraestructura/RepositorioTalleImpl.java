package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioTalle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioTalle")
public class RepositorioTalleImpl implements RepositorioTalle {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioTalleImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Talle> obtenerTalles() {
        String hql = "FROM Talle";
        return sessionFactory.getCurrentSession().createQuery(hql).getResultList();
    }

    @Override
    public void crearOActualizarTalle(Talle talle) {
        sessionFactory.getCurrentSession().saveOrUpdate(talle);
    }

    @Override
    public Talle obtenerTalle(Long id) {
        return sessionFactory.getCurrentSession().get(Talle.class, id);
    }

    @Override
    public void borrarTalle(Talle talle) {
        sessionFactory.getCurrentSession().delete(talle);
    }

    @Override
    public List<Talle> buscarTallesDePrendaPorId(Long idPrenda) {
        final Session session = sessionFactory.getCurrentSession();

        return session.createCriteria(Talle.class)
                .add(Restrictions.eq("prenda.id", idPrenda))
                .list();
    }

    @Override
    public List<Talle> buscarTallesPorPais(String pais) {
        final Session session= sessionFactory.getCurrentSession();
        return session.createCriteria(Talle.class)
                .add(Restrictions.eq("pais", pais))
                .list();
    }

    /*
    @Override
    public Talle buscarTallePorId(Long id) {
        return (Talle) sessionFactory.getCurrentSession()
                .createCriteria(Talle.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
    */
}
