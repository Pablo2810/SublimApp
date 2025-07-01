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

    @Override
    public List<Tela> listarTelas() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Tela", Tela.class)
                .getResultList();
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

    @Override
    public List<Tela> listarTelasDeFabrica() {
        String hql = "FROM Tela t WHERE t.id NOT IN (SELECT tu.id FROM TelaUsuario tu)";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Tela.class)
                .getResultList();
    }

    @Override
    public Tela buscarTelasDelUsuario(Long id, Usuario usuario) {
        return (TelaUsuario) sessionFactory.getCurrentSession()
                .createCriteria(TelaUsuario.class)
                .add(Restrictions.eq("id", id))
                .add(Restrictions.eq("usuario", usuario))
                .uniqueResult();
    }

    @Override
    public List<Tela> obtenerTelasDelUsuario(Usuario usuario) {
        return sessionFactory.getCurrentSession()
                .createCriteria(TelaUsuario.class)
                .add(Restrictions.eq("usuario", usuario))
                .add(Restrictions.eq("esManual", true))
                .list();
    }

    @Override
    public List<Tela> buscarTelasDePrendaPorIdPrenda(Long prendaId) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Tela.class)
                .createAlias("prendas", "p")
                .add(Restrictions.eq("p.id", prendaId))
                .list();
    }

    @Override
    public List<Tela> buscarTelasDePrendaConMetrosSuficientesPorIdPrenda(Long prendaId, Double metrosTalle) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Tela.class)
                .createAlias("prendas", "p")
                .add(Restrictions.eq("p.id", prendaId))
                .add(Restrictions.ge("metros", metrosTalle))
                .list();
    }

    @Override
    public void guardarTelaFabrica() {
        // no implementado aún
    }
}

