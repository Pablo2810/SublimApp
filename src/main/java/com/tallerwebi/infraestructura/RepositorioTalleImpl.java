package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Talle;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioTalle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioTalle")
public class RepositorioTalleImpl implements RepositorioTalle {
    private SessionFactory sessionFactory;


    public RepositorioTalleImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Talle> buscarPorID(Long idPrenda) {
        final Session session = sessionFactory.getCurrentSession();

        return session.createCriteria(Talle.class)
                .add(Restrictions.eq("prenda.id", idPrenda))
                .list();
    }

}
