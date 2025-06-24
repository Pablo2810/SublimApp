package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Tela;
import com.tallerwebi.dominio.entidad.TelaUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioTela")
public class RepositorioTelaImpl implements RepositorioTela {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioTelaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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
    public void borrarTela(Tela tela) {
        sessionFactory.getCurrentSession().delete(tela);
    }

    /*
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

    @Override
    public Tela buscarTelaPorId(Long id, Usuario usuario) {
        return (TelaUsuario) sessionFactory.getCurrentSession()
                .createCriteria(TelaUsuario.class)
                .add(Restrictions.eq("id", id))
                .add(Restrictions.eq("usuario", usuario))
                .uniqueResult();
    }
     */

}
