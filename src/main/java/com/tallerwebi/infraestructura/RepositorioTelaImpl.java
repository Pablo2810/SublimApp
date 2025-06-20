package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioTela")
public class RepositorioTelaImpl implements RepositorioTela {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioTelaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarTelaFabrica() {

    }

    @Override
    public List<Tela> buscarTelasPorPrenda(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Tela.class)
                .createAlias("prendas", "p")
                .add(Restrictions.eq("p.id", id))
                .list();
    }
}
