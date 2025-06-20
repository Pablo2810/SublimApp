package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioTelaImpl implements RepositorioTela {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Tela> listarTelas() {
        String hql = "FROM Tela";
        return sessionFactory.getCurrentSession().createQuery(hql).getResultList();
    }

    @Override
    public Tela obtenerTela(Long id) {
        return sessionFactory.getCurrentSession().get(Tela.class, id);
    }

    @Override
    public void crearOActualizarTela(Tela tela) {
        sessionFactory.getCurrentSession().saveOrUpdate(tela);
    }

    @Override
    public void borrarTela(Long id) {
        sessionFactory.getCurrentSession().delete(id);
    }

}
