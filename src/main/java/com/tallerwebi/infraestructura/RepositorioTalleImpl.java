package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.repositorio.RepositorioTalle;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioTalleImpl implements RepositorioTalle {

    @Autowired
    private SessionFactory sessionFactory;

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

}
