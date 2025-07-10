package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.repositorio.RepositorioTela;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioTela")
@Transactional
public class RepositorioTelaImpl implements RepositorioTela {

    @Autowired
    SessionFactory sessionFactory;

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
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(tela);
        session.flush();  // <-- fuerza la escritura inmediata en la BD
    }
    
    @Override
    public void borrarTela(Tela tela) {
        sessionFactory.getCurrentSession().delete(tela);
    }

    @Override
    public List<Tela> listarTelasDeFabrica() {
        String hql = "FROM com.tallerwebi.dominio.entidad.Tela t WHERE t.id NOT IN (SELECT tu.id FROM com.tallerwebi.dominio.entidad.TelaUsuario tu)";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Tela.class)
                .getResultList();
    }

    @Override
    public TelaUsuario buscarTelaUsuarioPorTipoYColor(Usuario usuario, TipoTela tipoTela, String color) {
        String hql = "FROM TelaUsuario tu WHERE tu.usuario = :usuario AND tu.tipoTela = :tipoTela AND tu.color = :color";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, TelaUsuario.class)
                .setParameter("usuario", usuario)
                .setParameter("tipoTela", tipoTela)
                .setParameter("color", color)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public List<TelaUsuario> buscarTelasUsuarioPorUsuarioYEstado(Long usuarioId, EstadoTela estado) {
        String hql = "FROM TelaUsuario tu WHERE tu.usuario.id = :usuarioId AND tu.estado = :estado";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, TelaUsuario.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("estado", estado)
                .getResultList();
    }

    @Override
    public List<TelaUsuario> buscarTelasUsuarioPorUsuario(Long usuarioId) {
        String hql = "FROM TelaUsuario tu WHERE tu.usuario.id = :usuarioId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql, TelaUsuario.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

}

