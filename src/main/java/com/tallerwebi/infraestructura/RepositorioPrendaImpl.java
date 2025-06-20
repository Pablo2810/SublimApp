package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Prenda;
import com.tallerwebi.dominio.repositorio.RepositorioPrenda;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioPrenda")
public class RepositorioPrendaImpl implements RepositorioPrenda {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPrendaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Prenda> obtener() {
        return sessionFactory.getCurrentSession().createCriteria(Prenda.class).list();
    }
}
